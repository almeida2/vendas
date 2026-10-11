package com.fatec.vendas.servico;

import java.util.List;
import com.fatec.vendas.model.Produto;

public interface IProdutoService {
    Produto cadastrarProduto(Produto produto);

    List<Produto> listarTodos();
}