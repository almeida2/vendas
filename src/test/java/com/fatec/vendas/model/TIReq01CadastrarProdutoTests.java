package com.fatec.vendas.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.fatec.vendas.servico.IProdutoRepository;

// A anotacao @DataJpaTest permite testar as operacoes de banco de dados
// configurando o contexto necessario para executar testes de repositorios JPA.
// evitando o custo de carregar a aplicação inteira
@DataJpaTest
public class TIReq01CadastrarProdutoTests {
    // O objetivo principal para validar a integração da classe Produto
    // com o Spring Data JPA Repository, é testar se o mapeamento ORM
    // (anotações de entidade, validações de coluna, IDs) e as consultas SQL
    // funcionam perfeitamente no banco de dados.
    @Autowired
    IProdutoRepository produtoRepository;

    @Test
    public void ct01_quando_informacoes_validas_cadastro_com_sucesso() {
        // DADO que a informacoes de produto sao validas
        Produto produto = ProdutoTestBuilder.umProduto().build();
        // QUANDO o usuario cadastra o produto
        produtoRepository.save(produto);
        // ENTÃO o produto e cadastrado com sucesso e listado no repositorio
        assertEquals(1, produtoRepository.count());
        var produtoRetornado = produtoRepository.findById(produto.getId()).get();
        assertNotNull(produtoRetornado.getId());
        assertEquals("Teclado Mecanico", produtoRetornado.getNome());
        assertEquals(250.00, produtoRetornado.getPreco());
        assertEquals(5, produtoRetornado.getQuantidadeEstoque());
    }

    @Test
    public void ct02_quando_nome_invalido_retorna_excecao() {
        // DADO que a informacoes de produto sao invalidas
        Produto produto = ProdutoTestBuilder.umProduto().comNome("").build();
        // QUANDO e ENTÃO o usuário tenta cadastrar o produto
        ConstraintViolationException exception = assertThrows(
                ConstraintViolationException.class,
                () -> produtoRepository.saveAndFlush(produto),
                "Deveria falhar ao cadastrar produto com nome vazio");

        // ENTÃO valida se a mensagem de erro esperada está contida na exceção
        // O Bean Validation encapsula a mensagem dentro de um conjunto de violações.
        boolean contemMensagem = exception.getConstraintViolations().stream()
                .anyMatch(v -> v.getMessage().equals("Nome do produto invalido"));

        assertTrue(contemMensagem, "A mensagem de validação esperada não foi encontrada");
    }
}
