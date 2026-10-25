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
public class TIManterProdutoTests {
    // O objetivo principal para validar a integração da classe Produto
    // com o Spring Data JPA Repository, é testar se o mapeamento ORM
    // (anotações de entidade, validações de coluna, IDs) e as consultas SQL
    // funcionam perfeitamente no banco de dados.
    @Autowired
    IProdutoRepository produtoRepository;

    @Test
    void ct01_quando_informacoes_validas_cadastro_com_sucesso() {
        // DADO que a informacoes de produto sao validas
        BuilderProduto builderProduto = BuilderProduto.umProduto();    
        // QUANDO o usuario cadastra o produto
        Produto produto = produtoRepository.save(builderProduto.build());
        // ENTÃO o produto e cadastrado com sucesso 
        assertEquals(1, produtoRepository.count());
        assertNotNull(produto.getId());
        assertEquals(produto.getId(), produto.getId());
        assertEquals("Teclado Mecanico", produto.getDescricao());
        assertEquals(250.00, produto.getPreco());
        assertEquals(5, produto.getQuantidadeEstoque());
    }
     @Test
    void ct02_quando_informacoes_validas_retorna_detalhes_do_produto() {
        // DADO que a informacoes de produto sao validas
        BuilderProduto builderProduto = BuilderProduto.umProduto();    
        // QUANDO o usuario consulta o produto
        Produto produto = produtoRepository.save(builderProduto.build());
        // ENTÃO os detalhes do produto sao retornados
        produtoRepository.findById(produto.getId()).ifPresent(p -> {
            assertNotNull(p.getId());
            assertEquals("Teclado Mecanico", p.getDescricao());
            assertEquals(250.00, p.getPreco());
            assertEquals(5, p.getQuantidadeEstoque());
        });
        
    }

}
