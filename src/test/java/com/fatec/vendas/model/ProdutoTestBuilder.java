package com.fatec.vendas.model;

public class ProdutoTestBuilder {
    // 1. Valores padrão
    private Long id = null;
    private String nome = "Teclado Mecanico";
    private double preco = 250.00;
    private int quantidadeEstoque = 5;

    // 2. Ponto de entrada estático para o Builder
    public static ProdutoTestBuilder umProduto() {
        return new ProdutoTestBuilder();
    }

    public ProdutoTestBuilder comId(Long id) {
        this.id = id;
        return this;
    }

    public ProdutoTestBuilder comNome(String nome) {
        this.nome = nome;
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
        produto.setId(id);
        produto.setNome(nome);
        produto.setPreco(preco);
        produto.setQuantidadeEstoque(quantidadeEstoque);
        return produto;
    }
}
