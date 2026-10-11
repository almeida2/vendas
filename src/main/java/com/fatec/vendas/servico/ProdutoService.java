package com.fatec.vendas.servico;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fatec.vendas.model.Produto;

@Service
public class ProdutoService implements IProdutoService {

    @Autowired
    private IProdutoRepository produtoRepository;

    public Produto cadastrarProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }
}