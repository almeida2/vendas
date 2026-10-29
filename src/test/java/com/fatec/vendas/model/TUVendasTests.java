package com.fatec.vendas.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.fatec.vendas.model.Produto;



public class TUVendasTests {
    Venda venda;
    @Test 
    public void ct01_cadastrar_venda_com_informacoes_validas() {
        Venda venda = BuilderVenda.umaVenda()
        .comCpf("59140006026")
        .comProduto(BuilderProduto.umProduto().build(), 2)
        .build();
        assertNotNull(venda);
    }
}
