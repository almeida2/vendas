package com.fatec.vendas.servico;

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

public class DescontoNaVenda {

    private static final Set<Month> MESES_PROMOCIONAIS = Set.of(
            Month.APRIL, Month.MAY, Month.NOVEMBER, Month.DECEMBER);

    private static final BigDecimal FRETE_FIXO = new BigDecimal("20.00");
    private static final BigDecimal FRETE_GRATIS = new BigDecimal("0.00");
     Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Valida os atributos de entrada e calcula o valor final da venda considerando
     * os descontos.
     *
     * @param primeiraCompraStr             Representação em String ("True"/"False")
     *                                      para primeira compra.
     * @param dataVendaStr                  Data no formato ISO (YYYY-MM-DD) ou
     *                                      DD/MM/YYYY.
     * @param valorTotalStr                 Representação textual do valor total.
     * @param valorFreteStr                 Representação textual do frete.
     * @param percentualDescontoPromocional Percentual informado para o mês
     *                                      promocional (ex: 0.10 para 10%).
     * @return Valor final recalculado com descontos.
     * @throws IllegalArgumentException caso qualquer entrada pertença a uma Classe
     *                                  Inválida.
     */
    public BigDecimal regraDeDesconto(
            String primeiraCompraStr,
            String dataVendaStr,
            String valorTotalStr,
            String valorFreteStr
        ) {
        

        // 1. Validação do Atributo: Primeira Compra
        boolean ePrimeiraCompra = validarEConverterPrimeiraCompra(primeiraCompraStr);

        // 2. Validação do Atributo: Data da Venda (Inclui checagem de formato e ano
        // bissexto)
        LocalDate dataVenda = validarEConverterData(dataVendaStr);

        // 3. Validação do Atributo: Valor Total
        BigDecimal valorTotal = validarEConverterValorTotal(valorTotalStr);

        // 4. Validação do Atributo: Valor do Frete
        BigDecimal valorFrete = validarEConverterFrete(valorFreteStr);

        // 5. Validação das Regras do Mês Promocional
        BigDecimal percentualDescontoPromocional = BigDecimal.ZERO;
         BigDecimal percentualDescontoTotal = BigDecimal.ZERO;
        boolean isMesPromocional = MESES_PROMOCIONAIS.contains(dataVenda.getMonth());
        logger.info(">>>>>> Executando o servico de desconto com os seguintes atributos: " + "mes promocional=>"
                + isMesPromocional + " percentual de desconto promocional: "
                + percentualDescontoPromocional + " primeira compra=>" + ePrimeiraCompra
                + " data da venda: " + dataVendaStr);
        // no me promocional o desconto não pode ser maior que 10% e fora do mes promocional o desconto não pode ser maior que 0
        if (isMesPromocional) {
            percentualDescontoPromocional = new BigDecimal("0.10");
            percentualDescontoTotal = percentualDescontoTotal.add(percentualDescontoPromocional);
        } else {
            percentualDescontoPromocional = BigDecimal.ZERO;
        }
        // --- Cálculo do Valor Final ---
        if (ePrimeiraCompra) {
            percentualDescontoTotal = percentualDescontoTotal.add(new BigDecimal("0.05"));
        }

        BigDecimal valorDesconto = valorTotal.multiply(percentualDescontoTotal);
        BigDecimal valorComDesconto = valorTotal.subtract(valorDesconto);

        return valorComDesconto.add(valorFrete).setScale(2, RoundingMode.HALF_UP);
    }

    private boolean validarEConverterPrimeiraCompra(String entrada) {
        if (entrada == null || entrada.trim().isEmpty()) {
            throw new IllegalArgumentException("Primeira compra não pode ser em branco ou vazia.");
        }
        String valorNormalizado = entrada.trim();
        if ("true".equalsIgnoreCase(valorNormalizado) || "sim".equalsIgnoreCase(valorNormalizado)) {
            return true;
        } else if ("false".equalsIgnoreCase(valorNormalizado) || "não".equalsIgnoreCase(valorNormalizado)
                || "nao".equalsIgnoreCase(valorNormalizado)) {
            return false;
        } else {
            throw new IllegalArgumentException("Valor inválido para Primeira Compra: " + entrada);
        }
    }

    private LocalDate validarEConverterData(String dataStr) {
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

    private BigDecimal validarEConverterValorTotal(String valorStr) {
        if (valorStr == null || valorStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Valor Total não pode ser em branco ou vazio.");
        }
        try {
            BigDecimal valor = new BigDecimal(valorStr.trim().replace(",", "."));
            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Valor Total deve ser maior que zero.");
            }
            return valor;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Formato numérico inválido para Valor Total: " + valorStr);
        }
    }

    private BigDecimal validarEConverterFrete(String freteStr) {
        if (freteStr == null || freteStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Valor do Frete não pode ser em branco ou vazio.");
        }
        try {
            BigDecimal frete = new BigDecimal(freteStr.trim().replace(",", "."));
            boolean isFreteValido = frete.compareTo(FRETE_GRATIS) == 0 || frete.compareTo(FRETE_FIXO) == 0;
            if (!isFreteValido) {
                throw new IllegalArgumentException("Valor do Frete deve ser exclusivamente R$ 0.00 ou R$ 20.00.");
            }
            return frete;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Formato numérico inválido para Valor do Frete: " + freteStr);
        }
    }

    
}
