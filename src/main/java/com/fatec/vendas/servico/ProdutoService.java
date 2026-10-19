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
        if (produto.getNome() == null || produto.getNome().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto invalido");
        }
        if (produto.getPreco() == 0 || produto.getPreco() < 0) {
            throw new IllegalArgumentException("Preco do produto invalido");
        }
        if (produto.getQuantidadeEstoque() == 0 || produto.getQuantidadeEstoque() < 0) {
            throw new IllegalArgumentException("Quantidade em estoque do produto invalida");
        }
        return produtoRepository.save(produto);
    }

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id).get();
    }
}