package com.fatec.vendas.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.fatec.vendas.servico.IProdutoRepository;

/**
 * Testes de Integração Orientados a Dados (Data-Driven Tests - DDTests)
 * para validação do comportamento da entidade Produto e ProdutoRepository
 * de acordo com as regras estabelecidas nas anotações (@NotBlank, @Positive),
 * consumindo a massa de dados do arquivo produtos.csv.
 */
@DataJpaTest
public class TIValidarCRUDProdutoDDTests {

    @Autowired
    IProdutoRepository produtoRepository;

    /**
     * Valida o comportamento das opreações CRUD com base nas anotações de
     * validação:
     * - Registros válidos (IDs 1 a 5): são persistidos com sucesso e passam pelas
     * operações de CRUD.
     * - Registros inválidos (IDs 6 a 8): disparam ConstraintViolationException com
     * a mensagem da anotação violada.
     */
    @ParameterizedTest(name = "CT [{0}]: \"{1}\" (R$ {2}, Estoque: {3})")
    @CsvFileSource(resources = "/produtos.csv", numLinesToSkip = 1)
    void validacao_do_comportamento_crud_cenarios_validos_invalidos(
            int id, String codigo, String descricao, double preco, int quantidadeEstoque) {

        Produto produto = ProdutoTestBuilder.umProduto()
                .comCodigo(codigo)
                .comDescricao(descricao)
                .comPreco(preco)
                .comQuantidadeEstoque(quantidadeEstoque)
                .build();

        if (isDadosValidos(codigo, descricao, preco, quantidadeEstoque)) {
            // ========================================================
            // CENÁRIO VÁLIDO: CREATE, READ, UPDATE, DELETE
            // ========================================================
            // 1. CREATE: Salva no repositório
            Produto produtoSalvo = produtoRepository.saveAndFlush(produto);

            // ENTÃO o produto é cadastrado com ID gerado
            assertNotNull(produtoSalvo.getId(), "O ID do produto deve ser gerado pelo banco");
            assertEquals(codigo, produtoSalvo.getCodigo());
            assertEquals(descricao, produtoSalvo.getDescricao());
            assertEquals(preco, produtoSalvo.getPreco());
            assertEquals(quantidadeEstoque, produtoSalvo.getQuantidadeEstoque());
            assertTrue(produtoRepository.existsById(produtoSalvo.getId()));

            Long idGerado = produtoSalvo.getId();

            // 2. READ: Consulta por ID
            Optional<Produto> produtoConsultado = produtoRepository.findById(idGerado);
            assertTrue(produtoConsultado.isPresent(), "O produto cadastrado deve ser encontrado pelo ID");
            assertEquals(codigo, produtoConsultado.get().getCodigo());
            assertEquals(descricao, produtoConsultado.get().getDescricao());
            assertEquals(preco, produtoConsultado.get().getPreco());

            // 3. UPDATE: Atualiza atributos e salva
            produtoSalvo.setPreco(preco + 10.00);
            produtoRepository.saveAndFlush(produtoSalvo);
            assertEquals(preco + 10.00, produtoRepository.findById(idGerado).get().getPreco());

            // 4. DELETE: Exclui o produto
            produtoRepository.deleteById(idGerado);
            produtoRepository.flush();
            assertFalse(produtoRepository.existsById(idGerado), "O produto deve ser excluído com sucesso");
            assertTrue(produtoRepository.findById(idGerado).isEmpty());

        } else {
            // ========================================================
            // CENÁRIO INVÁLIDO: VIOLAÇÃO DAS ANOTAÇÕES DE VALIDAÇÃO
            // ========================================================
            // QUANDO e ENTÃO o usuário tenta cadastrar produto inválido
            ConstraintViolationException exception = assertThrows(
                    ConstraintViolationException.class,
                    () -> produtoRepository.saveAndFlush(produto),
                    "Deveria falhar ao cadastrar produto com dados inválidos");

            // ENTÃO valida se a mensagem da anotação violada foi disparada
            String mensagemEsperada = obterMensagemViolacaoEsperada(codigo, descricao, preco, quantidadeEstoque);
            boolean contemMensagem = exception.getConstraintViolations().stream()
                    .anyMatch(v -> v.getMessage().equals(mensagemEsperada));

            assertTrue(contemMensagem,
                    "A mensagem de validação esperada [" + mensagemEsperada + "] não foi encontrada nas violações: "
                            + exception.getConstraintViolations());
        }
    }

    /**
     * Verifica se os dados atendem a todas as anotações da entidade Produto:
     * - codigo: @NotBlank (não nulo e não vazio/em branco)
     * - descricao: @NotBlank (não nulo e não vazio/em branco)
     * - preco: @Positive (maior que zero)
     * - quantidadeEstoque: @Positive (maior que zero)
     */
    private boolean isDadosValidos(String codigo, String descricao, double preco, int quantidadeEstoque) {
        return codigo != null && !codigo.trim().isEmpty() &&
                descricao != null && !descricao.trim().isEmpty() &&
                preco > 0 && quantidadeEstoque > 0;
    }

    /**
     * Identifica a mensagem de violação esperada de acordo com o atributo inválido.
     */
    private String obterMensagemViolacaoEsperada(String codigo, String descricao, double preco, int quantidadeEstoque) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return "Codigo do produto invalido";
        }
        if (descricao == null || descricao.trim().isEmpty()) {
            return "Descricao do produto invalida";
        }
        if (preco <= 0) {
            return "Preco do produto invalido";
        }
        if (quantidadeEstoque <= 0) {
            return "Quantidade em estoque do produto invalida";
        }
        return "";
    }
}
