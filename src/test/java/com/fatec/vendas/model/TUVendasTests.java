package com.fatec.vendas.model;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

class TUVendasTests {

    private Produto produto1;
    private Produto produto2;
    private LocalDate dtVenda;

    @BeforeEach
    void setUp() {
        produto1 = new Produto("P001", "Teclado Mecânico", 150.00, 10);
        produto2 = new Produto("P002", "Mouse Óptico", 80.00, 5);
        dtVenda = LocalDate.now();
    }

    @Test
    @DisplayName("Deve realizar uma venda com sucesso validando itens, estoque, total e relacionamento OneToMany")
    void deveRealizarVendaComSucesso() {
        // Arrange
        Venda venda = new Venda("12345678900");

        // Act
        venda.adicionarProduto(produto1, 2); // 2 * 150.00 = 300.00
        venda.adicionarProduto(produto2, 1); // 1 * 80.00 = 80.00

        // Assert - Validação do Relacionamento e Venda
        assertEquals(2, venda.getItens().size(), "A venda deve possuir exatamente 2 itens");
        assertEquals(380.00, venda.getTotalVenda(), 0.001, "O total da venda deve ser R$ 380,00");
        assertEquals(dtVenda, venda.getDataVenda(), "A data da venda deve ser a data atual (hoje)");

        // Assert - Validação individual dos Itens (Relacionamento bidirecional)
        ItemVenda item1 = venda.getItens().get(0);
        assertEquals(venda, item1.getVenda(), "O item deve ter a referência da venda pai");
        assertEquals(produto1, item1.getProduto(), "O item 1 deve estar associado ao produto 1");
        assertEquals(300.00, item1.getSubtotal(), 0.001, "Subtotal do item 1 deve ser R$ 300,00");

        ItemVenda item2 = venda.getItens().get(1);
        assertEquals(venda, item2.getVenda(), "O item deve ter a referência da venda pai");
        assertEquals(produto2, item2.getProduto(), "O item 2 deve estar associado ao produto 2");
        assertEquals(80.00, item2.getSubtotal(), 0.001, "Subtotal do item 2 deve ser R$ 80,00");

        // Assert - Validação da Baixa de Estoque
        assertEquals(8, produto1.getQuantidadeEstoque(), "O estoque do produto 1 deve ser atualizado para 8");
        assertEquals(4, produto2.getQuantidadeEstoque(), "O estoque do produto 2 deve ser atualizado para 4");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar vender produto sem estoque suficiente")
    void deveLancarExcecaoQuandoEstoqueInsuficiente() {
        Venda venda = new Venda("12345678900");

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> venda.adicionarProduto(produto2, 10) // Tentando comprar 10 mouses com apenas 5 em estoque
        );

        assertEquals("Quantidade para baixa de estoque inválida.", exception.getMessage());
        assertEquals(0, venda.getItens().size(), "Nenhum item deve ser adicionado em caso de erro");
        assertEquals(0.0, venda.getTotalVenda(), "O total da venda deve permanecer zerado");
    }
}