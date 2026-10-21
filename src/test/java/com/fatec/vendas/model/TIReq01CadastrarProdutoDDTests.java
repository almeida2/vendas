package com.fatec.vendas.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @ParameterizedTest(name = "CT [{0}]: \"{1}\" (R$ {2}, Estoque: {3})")
    @CsvFileSource(resources = "/produtos-pce.csv", numLinesToSkip = 1)
    void validar_cadastro_produto_cenarios_validos_invalidos(int id, String codigo, String descricao, double preco,
            int quantidadeEstoque) {

        Produto produto = ProdutoTestBuilder.umProduto()
                .comCodigo(codigo)
                .comDescricao(descricao)
                .comPreco(preco)
                .comQuantidadeEstoque(quantidadeEstoque)
                .build();

        if (isDadosValidos(codigo, descricao, preco, quantidadeEstoque)) {
            // Cenário Válido: Salvar com sucesso
            Produto produtoSalvo = produtoRepository.saveAndFlush(produto);
            assertNotNull(produtoSalvo.getId(), "O ID deve ser gerado automaticamente");
            assertEquals(codigo, produtoSalvo.getCodigo());
            assertEquals(descricao, produtoSalvo.getDescricao());
            assertEquals(preco, produtoSalvo.getPreco());
            assertEquals(quantidadeEstoque, produtoSalvo.getQuantidadeEstoque());
        } else {
            // Cenário Inválido: Validar exceção
            ConstraintViolationException exception = assertThrows(
                    ConstraintViolationException.class,
                    () -> produtoRepository.saveAndFlush(produto),
                    "Deveria falhar ao cadastrar produto com dados inválidos");

            String mensagemEsperada = obterMensagemViolacaoEsperada(codigo, descricao, preco, quantidadeEstoque);
            boolean contemMensagem = exception.getConstraintViolations().stream()
                    .anyMatch(v -> v.getMessage().equals(mensagemEsperada));

            assertTrue(contemMensagem, "Mensagem de validação esperada não encontrada");
        }
    }

    /**
     * Verifica se os dados atendem às validações da entidade.
     * - codigo: @NotBlank (não nulo e não vazio)
     * - descricao: @NotBlank (não nulo e não vazio)
     * - preco: @Positive (maior que 0)
     * - quantidadeEstoque: @Positive (maior que 0)
     */
    private boolean isDadosValidos(String codigo, String descricao, double preco, int quantidadeEstoque) {
        return codigo != null && !codigo.trim().isEmpty() &&
                descricao != null && !descricao.trim().isEmpty() &&
                preco > 0 &&
                quantidadeEstoque > 0;
    }

    /**
     * Obtém a mensagem de erro esperada para cada tipo de invalidez.
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
