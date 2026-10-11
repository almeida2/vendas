package com.fatec.vendas.servico;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import com.fatec.vendas.model.Produto;

@SpringBootTest
@DataJpaTest
public class TIReq01CadastrarProdutoTests {
    @Autowired
    ProdutoService produtoService;

    @Test
    public void ct01_quando_informacoes_validas_cadastro_com_sucesso() {
        // DADO que a compra não foi realizada em mes promocional, primeira compra true
        // e valor da compra<200
        String nome = "Teclado Mecanico";
        double preco = 250.00;
        int quantidadeEstoque = 5;
        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setPreco(preco);
        produto.setQuantidadeEstoque(quantidadeEstoque);
        produtoService.cadastrarProduto(produto);
        assertNotNull(produto.getId());
        assertEquals(nome, produto.getNome());
        assertEquals(preco, produto.getPreco());
        assertEquals(quantidadeEstoque, produto.getQuantidadeEstoque());
    }

    @Test
    public void ct02_quando_informacoes_validas_cadastro_com_sucesso() {
        // DADO que a compra não foi realizada em mes promocional, primeira compra true
        // e valor da compra<200
        String nome = "Teclado Mecanico";
        double preco = 250.00;
        int quantidadeEstoque = 5;
        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setPreco(preco);
        produto.setQuantidadeEstoque(quantidadeEstoque);
        produtoService.cadastrarProduto(produto);
        assertNotNull(produto.getId());
        assertEquals(nome, produto.getNome());
        assertEquals(preco, produto.getPreco());
        assertEquals(quantidadeEstoque, produto.getQuantidadeEstoque());
    }

}
