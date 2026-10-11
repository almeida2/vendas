package com.fatec.vendas.model;

import java.util.List;
import java.util.stream.Collectors;

public class VendaResponseDTO {
    private Long id;
    private double totalVenda;
    private List<ItemVendaResponseDTO> itens;

    public VendaResponseDTO(Venda venda) {
        this.id = venda.getId();
        this.totalVenda = venda.getTotalVenda();
        this.itens = venda.getItens().stream()
                .map(ItemVendaResponseDTO::new)
                .collect(Collectors.toList());
    }

    // Getters
    public Long getId() {
        return id;
    }

    public double getTotalVenda() {
        return totalVenda;
    }

    public List<ItemVendaResponseDTO> getItens() {
        return itens;
    }
}