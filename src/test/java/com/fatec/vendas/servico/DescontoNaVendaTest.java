package com.fatec.vendas.servico;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes de Equivalência para a Regra de Desconto na Venda")
class DescontoNaVendaTest {

    private DescontoNaVenda descontoNaVenda;

    @BeforeEach
    void setUp() {
        this.descontoNaVenda = new DescontoNaVenda();
    }

    @Nested
    @DisplayName("Cenários de Sucesso - Classes de Equivalência Válidas")
    class ClassesValidas {

        @Test
        @DisplayName("Deve aplicar desconto promocional de 10% em mês promocional (Novembro) sem primeira compra")
        void ct01_deveAplicarDescontoPromocionalValido() {
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
        @DisplayName("Deve aplicar desconto de Primeira Compra (5%) fora do mês promocional (Janeiro)")
        void ct02_deveAplicarDescontoPrimeiraCompraForaDoMesPromocional() {
            // DADO uma primeira compra realizada em mês não promocional (Janeiro)
            String primeiraCompra = "True";
            String dataVenda = "2026-01-10";
            String valorTotal = "200.00";
            String valorFrete = "0.00";
            

            // QUANDO a regra de desconto é validada
            BigDecimal resultado = descontoNaVenda.regraDeDesconto(
                    primeiraCompra, dataVenda, valorTotal, valorFrete);

            // ENTÃO deve aplicar 5% de primeira compra (200 - 10) + 0 de frete = 190.00
            assertEquals(new BigDecimal("190.00"), resultado);
        }

        @Test
        @DisplayName("Deve somar desconto promocional (10%) e primeira compra (5%) em mês promocional (Dezembro)")
        void ct03_deveSomarDescontoPromocionalEPrimeiraCompra() {
            // DADO uma primeira compra realizada no mês de Dezembro (Promocional)
            String primeiraCompra = "Sim";
            String dataVenda = "2026-12-25";
            String valorTotal = "100.00";
            String valorFrete = "20.00";


            // QUANDO a regra de desconto é calculada
            BigDecimal resultado = descontoNaVenda.regraDeDesconto(
                    primeiraCompra, dataVenda, valorTotal, valorFrete);

            // ENTÃO deve aplicar 15% de desconto total (100 - 15) + 20 de frete = 105.00
            assertEquals(new BigDecimal("105.00"), resultado);
        }

        @Test
        @DisplayName("Deve processar venda sem descontos quando não for primeira compra e fora de mês promocional")
        void ct04_deveProcessarVendaSemDescontos() {
            // DADO uma compra recorrente em mês não promocional (Julho) com frete de R$
            // 20,00
            String primeiraCompra = "Não";
            String dataVenda = "2026-07-20";
            String valorTotal = "150.00";
            String valorFrete = "20.00";

            // QUANDO o desconto é validado
            BigDecimal resultado = descontoNaVenda.regraDeDesconto(
                    primeiraCompra, dataVenda, valorTotal, valorFrete);

            // ENTÃO o valor final deve ser o valor total original + frete = 170.00
            assertEquals(new BigDecimal("170.00"), resultado);
        }
    }

    @Nested
    @DisplayName("Cenários de Exceção - Classes de Equivalência Inválidas")
    class ClassesInvalidas {

        @ParameterizedTest
        @ValueSource(strings = { "", " ", "Talvez", "123", "ABC" })
        @DisplayName("Deve rejeitar entradas inválidas para Primeira Compra")
        void ct05_deveLancarExcecaoParaPrimeiraCompraInvalida(String entradaInvalida) {
            // DADO um valor inválido para a opção de primeira compra
            // QUANDO e ENTÃO deve lançar IllegalArgumentException
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> descontoNaVenda.regraDeDesconto(
                            entradaInvalida, "2026-05-10", "100.00", "20.00"));
            assertTrue(exception.getMessage().toLowerCase().contains("primeira compra"));
        }

        

        

        @ParameterizedTest
        @ValueSource(strings = { "29/02/2026", "31/04/2026", "2026-13-01", "", " " })
        @DisplayName("Deve rejeitar formatos inválidos de data ou ano não bissexto (ex: 29/02/2026)")
        void ct07_deveLancarExcecaoParaDatasInvalidas(String dataInvalida) {
            // DADO uma data inválida ou em formato não suportado
            // QUANDO e ENTÃO deve lançar IllegalArgumentException
            assertThrows(
                    IllegalArgumentException.class,
                    () -> descontoNaVenda.regraDeDesconto(
                            "False", dataInvalida, "100.00", "20.00"));
        }

        @ParameterizedTest
        @ValueSource(strings = { "-50.00", "0.00", "", " ", "cem reais" })
        @DisplayName("Deve rejeitar Valor Total menor ou igual a zero ou em formato inválido")
        void ct08_deveLancarExcecaoParaValorTotalInvalido(String valorInvalido) {
            // DADO um valor total negativo, nulo ou textual
            // QUANDO e ENTÃO deve lançar IllegalArgumentException
            assertThrows(
                    IllegalArgumentException.class,
                    () -> descontoNaVenda.regraDeDesconto(
                            "False", "2026-05-10", valorInvalido, "20.00"));
        }

        @ParameterizedTest
        @ValueSource(strings = { "15.00", "25.00", "-5.00", "", " " })
        @DisplayName("Deve rejeitar Valor de Frete diferente de R$ 0.00 e R$ 20.00")
        void ct9_deveLancarExcecaoParaFreteInvalido(String freteInvalido) {
            // DADO um valor de frete fora do conjunto permitido {0.00, 20.00}
            // QUANDO e ENTÃO deve lançar IllegalArgumentException
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> descontoNaVenda.regraDeDesconto(
                            "False", "2026-05-10", "100.00", freteInvalido));
            assertTrue(exception.getMessage().contains("Valor do Frete"));
        }
    }
}
