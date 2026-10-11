package com.fatec.vendas.servico;

import com.fatec.vendas.model.Venda;
import com.fatec.vendas.model.VendaDTO;

public interface IVendaService {
    Venda realizarVenda(VendaDTO vendaDTO);

    String isPrimeiraCompra(String cpf);

}