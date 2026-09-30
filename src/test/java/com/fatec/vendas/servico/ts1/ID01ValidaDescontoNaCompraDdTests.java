package com.fatec.vendas.servico.ts1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class ID01ValidaDescontoNaCompraDdTests {

    DescontoNaVenda3 descontoNaVenda = new DescontoNaVenda3();

    @ParameterizedTest
    @CsvFileSource(files = "c:/temp/dataset-vendas.csv", numLinesToSkip = 1)
    void testRegraDeDesconto(int id, String primeiraCompra, String dataCompra, String valorCompra,
            String resultadoEsperado) {
        try {
            // Arrange & Act
            BigDecimal resultadoCalculado = descontoNaVenda.regraDeDesconto(primeiraCompra, dataCompra, valorCompra);

            // Assert
            assertEquals(new BigDecimal(resultadoEsperado), resultadoCalculado);

        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao calcular desconto para o ID: " + id + ": " + e.getMessage());
            assertEquals(resultadoEsperado, e.getMessage());
        }
    }
}
