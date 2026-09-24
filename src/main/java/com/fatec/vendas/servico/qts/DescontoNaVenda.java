package com.fatec.vendas.servico.qts;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class DescontoNaVenda {
    public BigDecimal regraDeDesconto( 
            String primeiraCompraStr,
            String dataVendaStr,
            String valorTotalStr ) {
        BigDecimal valorTotal = validarValorTotal(valorTotalStr);
 
        if (isPrimeiraCompra(primeiraCompraStr)) {
            BigDecimal valorDesconto = valorTotal.multiply(new BigDecimal("0.05"));
            valorTotal = valorTotal.subtract(valorDesconto);
        }
 
        if (validarData(dataVendaStr).getMonth() == LocalDate.now().getMonth()) {
            BigDecimal valorDesconto = valorTotal.multiply(new BigDecimal("0.10"));
            valorTotal = valorTotal.subtract(valorDesconto);
        }
 
        return valorTotal.setScale(2, RoundingMode.HALF_UP);
    }
 
    public boolean isPrimeiraCompra(String primeiraCompraStr) {
        if (primeiraCompraStr == null || primeiraCompraStr.isBlank() || primeiraCompraStr.isEmpty()) {
            throw new IllegalArgumentException("Primeira compra não pode ser nula ou vazia");
        }
        if (primeiraCompraStr.toLowerCase().equals("true")) {
            return true;
        }
        return false;
    }
 
    public LocalDate validarData(String dataVendaStr) {
        if (dataVendaStr == null || dataVendaStr.isBlank() || dataVendaStr.isEmpty()) {
            throw new IllegalArgumentException("Data de venda não pode ser nula ou vazia");
        }
        if (dataVendaStr.matches("\\d{2}/\\d{2}/\\d{4}")) {
            return LocalDate.parse(dataVendaStr);
        }
        return LocalDate.parse(dataVendaStr);
    }
 
    public BigDecimal validarValorTotal(String valorTotalStr) {
        if (valorTotalStr == null || valorTotalStr.isBlank() || valorTotalStr.isEmpty()) {
            throw new IllegalArgumentException("Valor total não pode ser nulo ou vazio");
        }
        return new BigDecimal(valorTotalStr);
    }
}
