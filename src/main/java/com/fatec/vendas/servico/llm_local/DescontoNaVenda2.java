package com.fatec.vendas.servico.llm_local;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DescontoNaVenda2 {

        /**
         * Calcula o desconto na venda aplicado à primeira compra, data de venda, valor
         * total e valor do frete.
         *
         * @param primeiraCompraStr se for "true" (ignorando maiúsculas/minúsculas)
         *                          aplicamos 5% de desconto sobre o valor total
         * @param dataVendaStr      no formato "YYYY-MM-DD" aplica desconto se data for
         *                          promocional
         * @param valorTotalStr     valor total dos produtos
         * @param valorFreteStr     valor do frete (opcional)
         * @return valor total final da compra com desconto e frete ajustado
         */
        public BigDecimal regraDeDesconto(String primeiraCompraStr, String dataVendaStr, String valorTotalStr,
                        String valorFreteStr) {
                BigDecimal valorTotal = new BigDecimal(valorTotalStr); // Convertendo o valor total para BigDecimal
                BigDecimal valorFrete = new BigDecimal(valorFreteStr); // Convertendo o valor do frete para BigDecimal,
                                                                       // se
                                                                       // fornecido

                // Processamento de primeira compra - se primeira compra true? aplica 5% senao
                // retorna zero
                BigDecimal descontoPrimeiraCompra = (primeiraCompraStr.equals("true")) ? BigDecimal.valueOf(0.05)
                                : BigDecimal.ZERO;

                // Processamento da data de venda
                LocalDate dataVenda = LocalDate.parse(dataVendaStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")); // Converte
                                                                                                                // a
                                                                                                                // data
                                                                                                                // para
                                                                                                                // LocalDate
                boolean dataPromocional = (dataVenda.getMonthValue() == 4) || (dataVenda.getMonthValue() == 5)
                                || (dataVenda.getMonthValue() == 11) ||
                                (dataVenda.getMonthValue() == 12); // Verifica se a data é promocional

                BigDecimal descontoData = BigDecimal.valueOf(dataPromocional ? 0.10 : 0); // Calcula desconto de data se
                                                                                          // for
                                                                                          // promocional

                BigDecimal valorFinal = valorTotal.subtract(descontoPrimeiraCompra).add(descontoData).setScale(2,
                                RoundingMode.HALF_UP);
                // Calcula o valor final com desconto

                // Calculo do valor do frete
                BigDecimal valorFreteAjustado = valorTotal.compareTo(BigDecimal.valueOf(200.00)) >= 0 ? BigDecimal.ZERO
                                : BigDecimal.valueOf(20.00);
                // Calcula o valor do frete

                // Valor final da compra
                BigDecimal valorFinalComFrete = valorFinal.add(valorFreteAjustado);

                return valorFinalComFrete; // Retorna o valor final com desconto e frete ajustado
        }
}