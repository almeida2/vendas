package com.fatec.vendas.util;
import com.fatec.vendas.model.Produto;
import com.fatec.vendas.model.Venda;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BuilderVenda {
    private Long id;
    private String cpf = "59140006026";
    private LocalDate dataVenda = LocalDate.now();
    private Double totalVenda;
    private final List<ItemPendente> itens = new ArrayList<>();

    public static BuilderVenda umaVenda() {
        return new BuilderVenda();
    }

    public BuilderVenda comId(Long id) {
        this.id = id;
        return this;
    }

    public BuilderVenda comCpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public BuilderVenda comDataVenda(LocalDate dataVenda) {
        this.dataVenda = dataVenda;
        return this;
    }

    public BuilderVenda comTotalVenda(double totalVenda) {
        this.totalVenda = totalVenda;
        return this;
    }

    public BuilderVenda comProduto(Produto produto, int quantidade) {
        itens.add(new ItemPendente(produto, quantidade));
        return this;
    }

    public Venda build() {
        Venda venda = new Venda(cpf);
        venda.setId(id);
        venda.setDataVenda(dataVenda);

        for (ItemPendente item : itens) {
            venda.adicionarProduto(item.produto(), item.quantidade());
        }

        if (totalVenda != null) {
            venda.setTotalVenda(totalVenda);
        }

        return venda;
    }

    private record ItemPendente(Produto produto, int quantidade) {
    }
}