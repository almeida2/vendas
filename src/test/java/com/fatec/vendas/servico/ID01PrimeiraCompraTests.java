package com.fatec.vendas.servico;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

//import com.fatec.vendas.servico.lm_local.DescontoNaVenda2;
//import com.fatec.vendas.servico.qts.DescontoNaVenda;
import com.fatec.vendas.servico.ts1.DescontoNaVenda;
import static org.junit.jupiter.api.Assertions.*;

class ID01PrimeiraCompraTests {

    private DescontoNaVenda descontoNaVenda;

    @BeforeEach
    void setUp() {
        this.descontoNaVenda = new DescontoNaVenda();
    }

    @Test
    void ct01_quando_data_nao_promocional_e_primeira_compra_true_deveAplicarDesconto() {
        // DADO que a compra não foi realizada em mes promocional, primeira compra true
        // e valor da compra<200
        String primeiraCompra = "true";
        String dataCompra = "15/01/2026";
        String valorCompra = "100.00";

        // QUANDO o usuario confirma a operação de compra
        BigDecimal resultado = descontoNaVenda.regraDeDesconto(primeiraCompra, dataCompra, valorCompra);

        // ENTÃO o valor a pagar considera 5% de desconto => valor total a pagar = (100
        // + 20) - 5 = 115.00
        assertEquals(new BigDecimal(0.05).setScale(2, RoundingMode.HALF_UP), resultado);

    }

    @Test
    void ct02_quando_data_nao_promocional_e_primeira_compra_false_nao_deve_aplicar_desconto() {
        // DADO que a compra não foi realizada em mes promocional, primeira compra false
        // e valor da compra<200
        String primeiraCompra = "false";
        String dataCompra = "15/01/2026";
        String valorCompra = "100.00";

        // QUANDO o usuario confirma a operação de compra
        BigDecimal resultado = descontoNaVenda.regraDeDesconto(primeiraCompra, dataCompra, valorCompra);

        // ENTÃO o valor a pagar é sem desconto
        assertEquals(new BigDecimal(0.00).setScale(2, RoundingMode.HALF_UP), resultado);

    }

    @Test
    void ct03_quando_data_nao_promocional_e_primeira_compra_true_deveAplicarDesconto() {
        // DADO que a compra não foi realizada em mes promocional, primeira compra true
        // e valor da compra>200
        String primeiraCompra = "true";
        String dataCompra = "15/01/2026";
        String valorCompra = "250.00";

        // QUANDO o usuario confirma a operação de compra
        BigDecimal resultado = descontoNaVenda.regraDeDesconto(primeiraCompra, dataCompra, valorCompra);

        // ENTÃO o valor a pagar considera 5% de desconto => valor total a pagar = (250
        // + 0) - 12.5 = 237.50
        assertEquals(new BigDecimal(0.05).setScale(2, RoundingMode.HALF_UP), resultado);

    }

    @Test
    void ct04_quando_primeira_compra_vazio_retorna_msg_de_erro() {
        // DADO que o atributo primeira compra esta vazio
        String primeiraCompra = "";
        String dataCompra = "2026-01-27";
        String valorCompra = "100.00";

        // QUANDO o usuario confirma a operação de compra
        try {
            BigDecimal resultado = descontoNaVenda.regraDeDesconto(
                    primeiraCompra, dataCompra, valorCompra);
        } catch (IllegalArgumentException e) {

            // ENTÃO retorna mensagem de erro

            assertEquals("Primeira compra não pode estar em branco ou vazia.", e.getMessage());
        }

    }

    @Test
    void ct05_quando_primeira_compra_em_branco_retorna_msg_de_erro() {
        // DADO que a primeira compra esta em branco
        String primeiraCompra = " ";
        String dataCompra = "2026-01-27";
        String valorCompra = "100.00";

        // QUANDO o usuario confirma a operação de compra
        try {
            BigDecimal resultado = descontoNaVenda.regraDeDesconto(
                    primeiraCompra, dataCompra, valorCompra);
        } catch (IllegalArgumentException e) {
            // ENTÃO retorna mensagem de erro
            assertEquals("Primeira compra não pode estar em branco ou vazia.", e.getMessage());
        }

    }

    @Test
    void ct06_quando_primeira_compra_valor_invalido_retorna_msg_de_erro() {
        // DADO que a primeira compra tem valor invalido
        String primeiraCompra = "aaa";
        String dataVenda = "2026-01-15";
        String valorCompra = "100.00";

        // QUANDO o usuario confirma a operação de compra
        try {
            BigDecimal resultado = descontoNaVenda.regraDeDesconto(
                    primeiraCompra, dataVenda, valorCompra);
        } catch (IllegalArgumentException e) {

            // ENTÃO retorna mensagem de erro

            assertEquals("Primeira compra deve ser true ou false.", e.getMessage());
        }

    }

    @Test
    void ct07_quando_primeira_compra_null_retorna_msg_de_erro() {
        // DADO que a primeira compra é null
        String primeiraCompra = null;
        String dataVenda = "2026-01-15";
        String valorCompra = "100.00";

        // QUANDO o usuario confirma a operação de compra
        try {
            BigDecimal resultado = descontoNaVenda.regraDeDesconto(
                    primeiraCompra, dataVenda, valorCompra);
        } catch (IllegalArgumentException e) {

            // ENTÃO retorna mensagem de erro

            assertEquals("Primeira compra não pode estar em branco ou vazia.", e.getMessage());
        }

    }

    @Test
    void ct08_quando_primeira_compra_True_retorna_msg_erro() {
        // DADO que a primeira compra é True
        String primeiraCompra = "True";
        String dataVenda = "2026-01-15";
        String valorCompra = "100.00";

        // QUANDO o usuario confirma a operação de compra
        try {
            BigDecimal resultado = descontoNaVenda.regraDeDesconto(
                    primeiraCompra, dataVenda, valorCompra);
        } catch (IllegalArgumentException e) {

            // ENTÃO retorna mensagem de erro

            assertEquals("Primeira compra deve ser true ou false.", e.getMessage());
        }

    }
}