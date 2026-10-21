package com.fatec.vendas.servico;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fatec.vendas.model.Produto;

@SpringBootTest
public class TIReq01CadastrarProdutoTests {
    @Autowired
    ProdutoService produtoService;
    Produto produto;

    @BeforeEach
    public void setup() {
        produto = new Produto();
        produto.setCodigo("PROD001");
        produto.setDescricao("Teclado Mecanico");
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
        assertEquals(produto.getCodigo(), p.getCodigo());
        assertEquals(produto.getDescricao(), p.getDescricao());
        assertEquals(produto.getPreco(), p.getPreco());
        assertEquals(produto.getQuantidadeEstoque(), p.getQuantidadeEstoque());
    }

    @Test
    public void ct02_quando_descricao_invalida_retorna_excecao() {
        // DADO que a descricao do produto e invalida
        // QUANDO o cadastro ou atribuicao e realizado
        try {
            produto.setDescricao("");
            produtoService.cadastrarProduto(produto);
            fail("Deveria falhar com descricao invalida");
        } catch (IllegalArgumentException e) {
            // ENTÃO a excecao esperada e capturada
            assertEquals("Descricao do produto invalida", e.getMessage());
        }
    }

    @Test
    public void ct03_quando_codigo_invalido_retorna_excecao() {
        // DADO que o codigo do produto e invalido
        // QUANDO o cadastro ou atribuicao e realizado
        try {
            produto.setCodigo("");
            produtoService.cadastrarProduto(produto);
            fail("Deveria falhar com codigo invalido");
        } catch (IllegalArgumentException e) {
            // ENTÃO a excecao esperada e capturada
            assertEquals("Codigo do produto invalido", e.getMessage());
        }
    }
}
