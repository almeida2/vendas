package com.fatec.vendas.servico;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fatec.vendas.model.Produto;

@Service
public class ProdutoService implements IProdutoService {

    private final IProdutoRepository produtoRepository;

    public ProdutoService(IProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Produto cadastrarProduto(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto nao pode ser nulo");
        }
        if (produto.getCodigo() == null || produto.getCodigo().trim().isEmpty()) {
            throw new IllegalArgumentException("Codigo do produto invalido");
        }
        if (produto.getDescricao() == null || produto.getDescricao().trim().isEmpty()) {
            throw new IllegalArgumentException("Descricao do produto invalida");
        }
        if (produto.getPreco() <= 0) {
            throw new IllegalArgumentException("Preco do produto invalido");
        }
        if (produto.getQuantidadeEstoque() <= 0) {
            throw new IllegalArgumentException("Quantidade em estoque do produto invalida");
        }
        return produtoRepository.save(produto);
    }

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id).orElse(null);
    }
}