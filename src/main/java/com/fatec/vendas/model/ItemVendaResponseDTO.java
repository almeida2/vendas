package com.fatec.vendas.model;

/**
 * ItemVendaResponseDTO
 */
public class ItemVendaResponseDTO {
    private Long id;
    private String produtoNome;
    private double produtoPreco;
    private int quantidade;
    private double subtotal;

    public ItemVendaResponseDTO(ItemVenda item) {
        this.id = item.getId();
        this.produtoNome = item.getProduto().getNome();
        this.produtoPreco = item.getProduto().getPreco();
        this.quantidade = item.getQuantidade();
        this.subtotal = item.getSubtotal();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public double getProdutoPreco() {
        return produtoPreco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getSubtotal() {
        return subtotal;
    }
}