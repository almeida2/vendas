package com.fatec.vendas.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class TUReq16ValidaDescontoNaCompraDdTests {

    DescontoNaVenda descontoNaVenda = new DescontoNaVenda();

    @ParameterizedTest
    @CsvFileSource(files = "c:/temp/vendas-pce.csv", numLinesToSkip = 1)
    void testRegraDeDesconto(int id, String primeiraCompra, String dataCompra, String valorCompra,
            String resultadoEsperado) {
        try {
            // Arrange & Act
            BigDecimal resultadoCalculado = descontoNaVenda.regraDeDesconto(primeiraCompra, dataCompra, valorCompra);

            // Assert
            assertEquals(new BigDecimal(resultadoEsperado), resultadoCalculado);

        } catch (IllegalArgumentException e) {
            assertEquals(resultadoEsperado, e.getMessage());
        }
    }
}
