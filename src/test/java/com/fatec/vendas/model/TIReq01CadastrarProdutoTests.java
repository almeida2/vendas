package com.fatec.vendas.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        Produto produto = new Produto();
        produto.setNome("Teclado Mecanico");
        produto.setPreco(250.00);
        produto.setQuantidadeEstoque(5);
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

}
