package com.fatec.vendas.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vendas")
public class Venda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemVenda> itens = new ArrayList<>();

    @Column(name = "cpf", nullable = false)
    private String cpf;

    @Column(name = "data_venda", nullable = false)
    private LocalDate dataVenda;

    @Column(name = "total_venda", nullable = false)
    private double totalVenda;

    public Venda() {
    }

    public Venda(String cpf) {
        this.cpf = cpf;
        this.dataVenda = LocalDate.now();
    }

    public void adicionarProduto(Produto produto, int quantidade) {
        ItemVenda item = new ItemVenda(produto, this, quantidade);
        this.itens.add(item);
        this.totalVenda += item.getSubtotal();
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ItemVenda> getItens() {
        return itens;
    }

    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
    }

    public double getTotalVenda() {
        return totalVenda;
    }

    public void setTotalVenda(double totalVenda) {
        this.totalVenda = totalVenda;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDate dataVenda) {
        this.dataVenda = dataVenda;
    }

}
