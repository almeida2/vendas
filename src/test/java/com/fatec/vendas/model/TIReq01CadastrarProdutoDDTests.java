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
public class TIReq01CadastrarProdutoDDTests {

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
    void ct01_quando_cadastra_produto_valida_comportamento_das_anotacoes(
            int id, String nome, double preco, int quantidadeEstoque) {

        Produto produto = ProdutoTestBuilder.umProduto()
                .comNome(nome)
                .comPreco(preco)
                .comQuantidadeEstoque(quantidadeEstoque)
                .build();

        if (isDadosValidos(nome, preco, quantidadeEstoque)) {
            // ========================================================
            // CENÁRIO VÁLIDO: CREATE, READ, UPDATE, DELETE
            // ========================================================
            // 1. CREATE: Salva no repositório
            Produto produtoSalvo = produtoRepository.saveAndFlush(produto);

            // ENTÃO o produto é cadastrado com ID gerado
            assertNotNull(produtoSalvo.getId(), "O ID do produto deve ser gerado pelo banco");
            assertEquals(nome, produtoSalvo.getNome());
            assertEquals(preco, produtoSalvo.getPreco());
            assertEquals(quantidadeEstoque, produtoSalvo.getQuantidadeEstoque());
            assertTrue(produtoRepository.existsById(produtoSalvo.getId()));

            Long idGerado = produtoSalvo.getId();

            // 2. READ: Consulta por ID
            Optional<Produto> produtoConsultado = produtoRepository.findById(idGerado);
            assertTrue(produtoConsultado.isPresent(), "O produto cadastrado deve ser encontrado pelo ID");
            assertEquals(nome, produtoConsultado.get().getNome());
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
            String mensagemEsperada = obterMensagemViolacaoEsperada(nome, preco, quantidadeEstoque);
            boolean contemMensagem = exception.getConstraintViolations().stream()
                    .anyMatch(v -> v.getMessage().equals(mensagemEsperada));

            assertTrue(contemMensagem,
                    "A mensagem de validação esperada [" + mensagemEsperada + "] não foi encontrada nas violações: "
                            + exception.getConstraintViolations());
        }
    }

    /**
     * Verifica se os dados atendem a todas as anotações da entidade Produto:
     * - nome: @NotBlank (não nulo e não vazio/em branco)
     * - preco: @Positive (maior que zero)
     * - quantidadeEstoque: @Positive (maior que zero)
     */
    private boolean isDadosValidos(String nome, double preco, int quantidadeEstoque) {
        return nome != null && !nome.trim().isEmpty() && preco > 0 && quantidadeEstoque > 0;
    }

    /**
     * Identifica a mensagem de violação esperada de acordo com o atributo inválido.
     */
    private String obterMensagemViolacaoEsperada(String nome, double preco, int quantidadeEstoque) {
        if (nome == null || nome.trim().isEmpty()) {
            return "Nome do produto invalido";
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
