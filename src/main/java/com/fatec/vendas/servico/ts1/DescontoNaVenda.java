package com.fatec.vendas.servico.ts1;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DescontoNaVenda {
    public BigDecimal regraDeDesconto(String primeiraCompra, String dataVenda, String valorTotal) {

        return new BigDecimal(0.05).setScale(2, RoundingMode.HALF_UP);
    }
}
