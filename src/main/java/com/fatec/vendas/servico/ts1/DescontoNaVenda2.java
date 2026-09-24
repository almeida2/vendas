package com.fatec.vendas.servico.ts1;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DescontoNaVenda2 {
    public BigDecimal regraDeDesconto(String primeiraCompra, String dataVenda, String valorTotal) {
        BigDecimal percentualDesconto = BigDecimal.ZERO;
        // 1. validar primeiraCompra (mes nao promocional)
        if (validarEConverterPrimeiraCompra(primeiraCompra)) {
            percentualDesconto = percentualDesconto.add(new BigDecimal("0.05").setScale(2, RoundingMode.HALF_UP));
        } else {
            percentualDesconto = new BigDecimal("0.00");
        }

        return percentualDesconto;
    }

    public boolean validarEConverterPrimeiraCompra(String entrada) {
        if (entrada == null || entrada.trim().isEmpty()) {
            throw new IllegalArgumentException("Primeira compra não pode estar em branco ou vazia.");
        } else {
            if (entrada.trim().equals("true"))
                return true;
            else if (entrada.trim().equals("false"))
                return false;
            else
                throw new IllegalArgumentException("Primeira compra deve ser true ou false.");
        }
    }
}
