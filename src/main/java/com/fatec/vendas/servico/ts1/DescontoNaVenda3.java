package com.fatec.vendas.servico.ts1;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DescontoNaVenda3 {
    Logger logger = LogManager.getLogger(this.getClass());
    private static final Set<Month> MESES_PROMOCIONAIS = Set.of(
            Month.APRIL, Month.MAY, Month.NOVEMBER, Month.DECEMBER);

    BigDecimal valorFrete = new BigDecimal("20.00");

    public BigDecimal regraDeDesconto(String primeiraCompra, String dataVenda, String valorTotal) {
        logger.info(">> Executando servico com os atributos: "
                + " primeiraCompra: " + primeiraCompra
                + " dataVenda: " + dataVenda
                + " valorTotal: " + valorTotal);
        BigDecimal percentualDesconto = BigDecimal.ZERO;
        // 1. validar primeiraCompra (mes nao promocional)
        if (validarEConverterPrimeiraCompra(primeiraCompra) && !isMesPromocional(dataVenda)) {
            percentualDesconto = percentualDesconto.add(new BigDecimal("0.05"));
        }
        // 2. validar mes promocional
        if (isMesPromocional(dataVenda)) {
            percentualDesconto = percentualDesconto.add(new BigDecimal("0.10"));
        }
        // 3. aplicar desconto
        BigDecimal valorTotalDecimal = new BigDecimal(valorTotal);
        BigDecimal valorDesconto = valorTotalDecimal.multiply(percentualDesconto);
        return valorTotalDecimal.subtract(valorDesconto).add(valorFrete).setScale(2, RoundingMode.HALF_UP);
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

    public boolean isMesPromocional(String dataStr) {
        LocalDate dataValida = validarEConverterData(dataStr);
        if (dataValida.getMonth().equals(Month.APRIL) ||
                dataValida.getMonth().equals(Month.MAY) ||
                dataValida.getMonth().equals(Month.NOVEMBER) ||
                dataValida.getMonth().equals(Month.DECEMBER)) {
            logger.info(">> E mes promocional? => true");
            return true;
        }
        logger.info(">> E mes promocional? => false");
        return false;
    }

    public LocalDate validarEConverterData(String dataStr) {
        if (dataStr == null || dataStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Data da venda não pode ser nula, branca ou vazia.");
        }
        try {
            // Tenta parsing nos formatos ISO (YYYY-MM-DD) ou PT-BR (DD/MM/YYYY)
            if (dataStr.contains("/")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu")
                        .withResolverStyle(ResolverStyle.STRICT);
                return LocalDate.parse(dataStr, formatter);
            }
            return LocalDate.parse(dataStr);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Data inválida ou em formato incorreto: " + dataStr, e);
        }
    }
}
