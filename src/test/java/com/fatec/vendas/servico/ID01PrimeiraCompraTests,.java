package com.fatec.vendas.servico;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ID01PrimeiraCompraTests {

    private DescontoNaVenda descontoNaVenda;

    @BeforeEach
    void setUp() {
        this.descontoNaVenda = new DescontoNaVenda();
    }

    @Test
    void ct01_quando_data_nao_mes_promocional_e_primeira_compra_true_deveAplicarDesconto() {
        // DADO que a venda não foi realizada em mes promocional e primeira compra do
        // cliente true
        String primeiraCompra = "True";
        String dataVenda = "2026-01-27";
        String valorTotal = "100.00";
        String valorFrete = "20.00";

        // QUANDO o usuario confirma a operação de compra (o sistema calcula o valor da
        // venda antes do pagamento)
        BigDecimal resultado = descontoNaVenda.regraDeDesconto(
                primeiraCompra, dataVenda, valorTotal, valorFrete);

        // ENTÃO o valor total deve considerar 5% de desconto (120 - 5) = 115.00
        assertEquals(new BigDecimal("115.00"), resultado);

    }

    @Test
    void ct02_quando_data_nao_mes_promocional_e_primeira_compra_false_nao_deve_aplicar_desconto() {
        // DADO que a venda não foi realizada em mes promocional e primeira compra do
        // cliente false
        String primeiraCompra = "False";
        String dataVenda = "2026-01-27";
        String valorTotal = "100.00";
        String valorFrete = "20.00";

        // QUANDO o usuario confirma a operação de compra (o sistema calcula o valor da
        // venda antes do pagamento)
        BigDecimal resultado = descontoNaVenda.regraDeDesconto(
                primeiraCompra, dataVenda, valorTotal, valorFrete);

        // ENTÃO o valor total é sem desconto
        assertEquals(new BigDecimal("120.00"), resultado);

    }

    @Test
    void ct03_quando_primeira_compra_em_branco_retorna_msg_de_erro() {
        // DADO que a primeira compra esta em branco
        String primeiraCompra = "";
        String dataVenda = "2026-01-27";
        String valorTotal = "100.00";
        String valorFrete = "20.00";

        // QUANDO o usuario confirma a operação de compra
        try {
            BigDecimal resultado = descontoNaVenda.regraDeDesconto(
                    primeiraCompra, dataVenda, valorTotal, valorFrete);
        } catch (IllegalArgumentException e) {

            // ENTÃO retorna mensagem de erro

            assertEquals("Primeira compra não pode estar em branco ou vazia.", e.getMessage());
        }

    }

    @Test
    void ct04_quando_primeira_compra_vazio_branco_retorna_msg_de_erro() {
        // DADO que a primeira compra esta vazio
        String primeiraCompra = " ";
        String dataVenda = "2026-01-27";
        String valorTotal = "100.00";
        String valorFrete = "20.00";

        // QUANDO o usuario confirma a operação de compra
        try {
            BigDecimal resultado = descontoNaVenda.regraDeDesconto(
                    primeiraCompra, dataVenda, valorTotal, valorFrete);
        } catch (IllegalArgumentException e) {

            // ENTÃO retorna mensagem de erro

            assertEquals("Primeira compra não pode estar em branco ou vazia.", e.getMessage());
        }

    }

    @Test
    void ct05_quando_primeira_compra_valor_invalido_retorna_msg_de_erro() {
        // DADO que a primeira compra tem valor invalido
        String primeiraCompra = "aaa";
        String dataVenda = "2026-01-27";
        String valorTotal = "100.00";
        String valorFrete = "20.00";

        // QUANDO o usuario confirma a operação de compra
        try {
            BigDecimal resultado = descontoNaVenda.regraDeDesconto(
                    primeiraCompra, dataVenda, valorTotal, valorFrete);
        } catch (IllegalArgumentException e) {

            // ENTÃO retorna mensagem de erro

            assertEquals("Valor inválido para Primeira Compra: " + primeiraCompra, e.getMessage());
        }

    }

}