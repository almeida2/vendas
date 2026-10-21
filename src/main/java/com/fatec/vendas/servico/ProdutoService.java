package com.fatec.vendas.servico;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fatec.vendas.model.Produto;

import jakarta.transaction.Transactional;

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

    @Override
    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    @Transactional
    public Optional<Produto> atualizar(Long id, Produto produtoAtualizado) {
        return produtoRepository.findById(id).map(produto -> {
            produto.setCodigo(produtoAtualizado.getCodigo());
            produto.setDescricao(produtoAtualizado.getDescricao());
            produto.setPreco(produtoAtualizado.getPreco());
            produto.setQuantidadeEstoque(produtoAtualizado.getQuantidadeEstoque());
            return produtoRepository.save(produto);
        });
    }

    @Override
    public boolean deletar(Long id) {
        if (produtoRepository.existsById(id)) {
            produtoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}