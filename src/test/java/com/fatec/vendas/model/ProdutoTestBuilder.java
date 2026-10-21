package com.fatec.vendas.model;

import org.springframework.test.util.ReflectionTestUtils;

public class ProdutoTestBuilder {
    // 1. Valores padrao
    private Long id = null;
    private String codigo = "PROD001";
    private String descricao = "Teclado Mecanico";
    private double preco = 250.00;
    private int quantidadeEstoque = 5;

    // 2. Ponto de entrada estatico para o Builder
    public static ProdutoTestBuilder umProduto() {
        return new ProdutoTestBuilder();
    }

    public ProdutoTestBuilder comId(Long id) {
        this.id = id;
        return this;
    }

    public ProdutoTestBuilder comCodigo(String codigo) {
        this.codigo = codigo;
        return this;
    }

    public ProdutoTestBuilder comDescricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public ProdutoTestBuilder comNome(String nome) {
        this.descricao = nome;
        return this;
    }

    public ProdutoTestBuilder comPreco(double preco) {
        this.preco = preco;
        return this;
    }

    public ProdutoTestBuilder comQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
        return this;
    }

    public Produto build() {
        Produto produto = new Produto();
        if (id != null) {
            ReflectionTestUtils.setField(produto, "id", id);
        }
        ReflectionTestUtils.setField(produto, "codigo", codigo);
        ReflectionTestUtils.setField(produto, "descricao", descricao);
        ReflectionTestUtils.setField(produto, "preco", preco);
        ReflectionTestUtils.setField(produto, "quantidadeEstoque", quantidadeEstoque);
        return produto;
    }
}
