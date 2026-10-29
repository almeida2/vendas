package com.fatec.vendas.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class TUProdutoTests {

    @Test
    void ct01_deveCriarProdutoComSucessoQuandoDadosValidos() {
        Produto produto = new Produto("COD001", "Teclado Mecanico", 250.00, 10);
        produto.setId(1L);

        assertEquals(1L, produto.getId());
        assertEquals("COD001", produto.getCodigo());
        assertEquals("Teclado Mecanico", produto.getDescricao());
        assertEquals(250.00, produto.getPreco());
        assertEquals(10, produto.getQuantidadeEstoque());
    }

    @Test
    void ct02_deveLancarExcecaoQuandoCodigoNuloOuVazio() {
        Produto produto = new Produto();

        IllegalArgumentException exNulo = assertThrows(
                IllegalArgumentException.class,
                () -> produto.setCodigo(null));
        assertEquals("O código do produto não pode ser nulo ou vazio.", exNulo.getMessage());

        IllegalArgumentException exVazio = assertThrows(
                IllegalArgumentException.class,
                () -> produto.setCodigo("   "));
        assertEquals("O código do produto não pode ser nulo ou vazio.", exVazio.getMessage());

        IllegalArgumentException exEspacoUnicode = assertThrows(
                IllegalArgumentException.class,
                () -> produto.setCodigo(" "));
        assertEquals("O código do produto não pode ser nulo ou vazio.", exEspacoUnicode.getMessage());
    }

    @Test
    void ct03_deveLancarExcecaoQuandoDescricaoNulaOuVazia() {
        Produto produto = new Produto();

        IllegalArgumentException exNulo = assertThrows(
                IllegalArgumentException.class,
                () -> produto.setDescricao(null));
        assertEquals("A descrição do produto não pode ser nula ou vazia.", exNulo.getMessage());

        IllegalArgumentException exVazio = assertThrows(
                IllegalArgumentException.class,
                () -> produto.setDescricao("   "));
        assertEquals("A descrição do produto não pode ser nula ou vazia.", exVazio.getMessage());

        // Rejeita descricao vazia
        IllegalArgumentException exNome = assertThrows(
                IllegalArgumentException.class,
                () -> produto.setDescricao(""));
        assertEquals("A descrição do produto não pode ser nula ou vazia.", exNome.getMessage());

        IllegalArgumentException exEspacoUnicode = assertThrows(
                IllegalArgumentException.class,
                () -> produto.setDescricao(" "));
        assertEquals("A descrição do produto não pode ser nula ou vazia.", exEspacoUnicode.getMessage());
    }

    @Test
    void ct04_deveLancarExcecaoQuandoPrecoNaoForPositivoOuFinito() {
        Produto produto = new Produto();

        IllegalArgumentException exZero = assertThrows(
                IllegalArgumentException.class,
                () -> produto.setPreco(0));
        assertEquals("Preço do produto invalido.", exZero.getMessage());

        IllegalArgumentException exNegativo = assertThrows(
                IllegalArgumentException.class,
                () -> produto.setPreco(-10.0));
        assertEquals("Preço do produto invalido.", exNegativo.getMessage());

        IllegalArgumentException exNaN = assertThrows(
                IllegalArgumentException.class,
                () -> produto.setPreco(Double.NaN));
        assertEquals("Preço do produto invalido.", exNaN.getMessage());

        IllegalArgumentException exInfinito = assertThrows(
                IllegalArgumentException.class,
                () -> produto.setPreco(Double.POSITIVE_INFINITY));
        assertEquals("Preço do produto invalido.", exInfinito.getMessage());
    }

    @Test
    void ct05_deveLancarExcecaoQuandoQuantidadeEstoqueZeroOuNegativa() {
        Produto produto = new Produto();

        IllegalArgumentException exZero = assertThrows(
                IllegalArgumentException.class,
                () -> produto.setQuantidadeEstoque(0));
        assertEquals("Quantidade para baixa de estoque invalida.", exZero.getMessage());

        IllegalArgumentException exNegativo = assertThrows(
                IllegalArgumentException.class,
                () -> produto.setQuantidadeEstoque(-5));
        assertEquals("Quantidade para baixa de estoque invalida.", exNegativo.getMessage());
    }

    @Test
    void ct06_deveLancarExcecaoQuandoIdMenorOuIgualAZero() {
        Produto produto = new Produto();

        IllegalArgumentException exZero = assertThrows(
                IllegalArgumentException.class,
                () -> produto.setCodigo(""));
        assertEquals("O código do produto não pode ser nulo ou vazio.", exZero.getMessage());

        IllegalArgumentException exNegativo = assertThrows(
                IllegalArgumentException.class,
                () -> produto.setCodigo(null));
        assertEquals("O código do produto não pode ser nulo ou vazio.", exNegativo.getMessage());
    }

    @Test
    void ct07_deveBaixarEstoqueComSucesso() {
        Produto produto = new Produto("COD001", "Mouse", 50.0, 10);
        produto.baixarEstoque(4);
        assertEquals(6, produto.getQuantidadeEstoque());
    }

    @Test
    void ct08_deveLancarExcecaoAoBaixarEstoqueComQuantidadeInvalida() {
        Produto produto = new Produto("COD001", "Mouse", 50.0, 10);

        IllegalArgumentException exZero = assertThrows(
                IllegalArgumentException.class,
                () -> produto.baixarEstoque(0));
        assertEquals("Quantidade para baixa de estoque invalida.", exZero.getMessage());

        IllegalArgumentException exInsuficiente = assertThrows(
                IllegalArgumentException.class,
                () -> produto.baixarEstoque(15));
        assertEquals("Quantidade para baixa de estoque invalida.", exInsuficiente.getMessage());
    }
}