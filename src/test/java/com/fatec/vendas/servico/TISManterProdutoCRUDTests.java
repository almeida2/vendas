package com.fatec.vendas.servico;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fatec.vendas.util.BuilderProduto;
import com.fatec.vendas.model.Produto;

import java.util.Optional;

@SpringBootTest
public class TISManterProdutoCRUDTests {
    @Autowired
    ProdutoService produtoService;
    @Autowired 
    IProdutoRepository produtoRepository;
    @BeforeEach 
    void setup() {
        // Limpa o repositório antes de cada teste para garantir um estado limpo
        produtoRepository.deleteAll();
    }
    @Test

    void ct01_quando_informacoes_validas_cadastro_com_sucesso() {
        // DADO que as informacoes de produto sao validas
        BuilderProduto builderProduto = BuilderProduto.umProduto();
        Produto produto = builderProduto.build();
        // QUANDO o usuario cadastra o produto
        Produto produtoRetornado = produtoService.cadastrarProduto(produto);
        // ENTÃO o produto e cadastrado com sucesso e listado no repositorio
        assertNotNull(produtoRetornado.getId());
        assertEquals("PROD001", produtoRetornado.getCodigo());
        assertEquals("Teclado Mecanico", produtoRetornado.getDescricao());
        assertEquals(250.00, produtoRetornado.getPreco());
        assertEquals(5, produtoRetornado.getQuantidadeEstoque());
    }

    @Test
    void ct02_quando_informacoes_validas_retorna_os_detalhes_a_consulta() {
        // DADO que a informacoes de produto sao validas
        BuilderProduto builderProduto = BuilderProduto.umProduto();
        Produto produto = builderProduto.build();
        Produto produtoRetornado = produtoService.cadastrarProduto(produto);
        // QUANDO o usuario consulta o produto
        Optional<Produto> produtoConsultado = produtoService.buscarPorId(produtoRetornado.getId());
        // ENTÃO os detalhes do produto sao retornados
        assertNotNull(produtoConsultado.get());
        assertEquals("PROD001", produtoConsultado.get().getCodigo());
        assertEquals("Teclado Mecanico", produtoConsultado.get().getDescricao());
        assertEquals(250.00, produtoConsultado.get().getPreco());
        assertEquals(5, produtoConsultado.get().getQuantidadeEstoque());
    }

}
