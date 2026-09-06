package com.fatec.vendas.servico;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DescontoNaVenda2Test {

    private DescontoNaVenda descontoNaVenda;

    @BeforeEach
    void setUp() {
        this.descontoNaVenda = new DescontoNaVenda();
    }

    @Test
    void ct01_quando_mes_promocional_deveAplicarDesconto() {
        // DADO que uma venda é realizada em mês promocional (Novembro)
        String primeiraCompra = "False";
        String dataVenda = "2026-11-15";
        String valorTotal = "100.00";
        String valorFrete = "20.00";

        // QUANDO o usuario confirma a operação de compra (o sistema calcula o valor da
        // venda antes do pagamento)
        BigDecimal resultado = descontoNaVenda.regraDeDesconto(
                primeiraCompra, dataVenda, valorTotal, valorFrete);

        // ENTÃO o valor total deve considerar 10% de desconto (100 - 10) + 20 de frete
        // = 110.00
        assertEquals(new BigDecimal("110.00"), resultado);

    }

    
     @Test
    void ct04_quando_data_em_branco_rejeitar_transacao() {
        // DADO que data da venda é invalida
        String primeiraCompra = "False";
        String dataVenda = "";
        String valorTotal = "100.00";
        String valorFrete = "20.00";
       

        // QUANDO o usuario confirma a operação de compra (o sistema calcula o valor da
        // venda antes do pagamento)
        try{    
            descontoNaVenda.regraDeDesconto(
                primeiraCompra, dataVenda, valorTotal, valorFrete);
        }catch(IllegalArgumentException e){
            //ENTAO - Deve apresentar a seguinte mensagem: Data inválida ou em formato incorreto: 2026-02-29
            assertEquals("Data da venda não pode ser nula, branca ou vazia.", e.getMessage());
        }

      
    }
   
}