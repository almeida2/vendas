package com.fatec.vendas.servico;

import java.util.List;
import java.util.Optional;

import com.fatec.vendas.model.Produto;

public interface IProdutoService {
    
    Produto cadastrarProduto(Produto produto);

    List<Produto> listarTodos();

    Optional<Produto> buscarPorId(Long id);
    
    Optional<Produto> atualizar(Long id, Produto produto);

    void deletar(Long id);
}
