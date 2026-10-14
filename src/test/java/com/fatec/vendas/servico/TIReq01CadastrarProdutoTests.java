package com.fatec.vendas.servico;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.fail;
import com.fatec.vendas.model.Produto;

@SpringBootTest
public class TIReq01CadastrarProdutoTests {
    @Autowired
    ProdutoService produtoService;
    Produto produto;

    @BeforeEach
    public void setup() {
        produto = new Produto();
        produto.setNome("Teclado Mecanico");
        produto.setPreco(250.00);
        produto.setQuantidadeEstoque(5);
    }

    @Test
    public void ct01_quando_informacoes_validas_cadastro_com_sucesso() {
        // DADO que as informacoes de produto sao validas
        // QUANDO o cadastrado é realizado
        Produto p = produtoService.cadastrarProduto(produto);
        // ENTAO o produto deve ser salvo no banco de dados
        assertNotNull(p);
        assertEquals(produto.getNome(), p.getNome());
        assertEquals(produto.getPreco(), p.getPreco());
        assertEquals(produto.getQuantidadeEstoque(), p.getQuantidadeEstoque());
    }
    
     @Test
    public void ct02_quando_nome_invalido_retorna_excecao() {
        // DADO que as informacoes de produto sao invalidas
        produto.setNome("");
        // QUANDO o cadastrado é realizado
        try {
            produtoService.cadastrarProduto(produto);
            fail("Deveria falhar com nome invalido");
        } catch (IllegalArgumentException e) {
            // ENTÃO o produto deve ser salvo no banco de dados
            assertEquals("Nome do produto invalido", e.getMessage());
        }
    }
    

}
