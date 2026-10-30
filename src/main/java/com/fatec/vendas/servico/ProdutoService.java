package com.fatec.vendas.servico;

import com.fatec.vendas.model.Produto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService implements IProdutoService {

    private static final Logger log = LoggerFactory.getLogger(ProdutoService.class);
    
    private final IProdutoRepository produtoRepository;
    

    public ProdutoService(IProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Override
    @Transactional
    public Produto cadastrarProduto(Produto produto) {
        log.debug("Iniciando validação para cadastro de produto: {}", produto);

        if (produto == null) {
            log.warn("Falha no cadastro: Objeto produto é nulo");
          
            throw new IllegalArgumentException("Produto nao pode ser nulo");
        }
        if (produto.getCodigo() == null || produto.getCodigo().trim().isEmpty()) {
            log.warn("Falha no cadastro: Código do produto é inválido");
          
            throw new IllegalArgumentException("Codigo do produto invalido");
        }
        if (produto.getDescricao() == null || produto.getDescricao().trim().isEmpty()) {
            log.warn("Falha no cadastro: Descrição é inválida para o código: {}", produto.getCodigo());
            
            throw new IllegalArgumentException("Descricao do produto invalida");
        }
        if (produto.getPreco() <= 0) {
            log.warn("Falha no cadastro: Preço R$ {} é inválido para o produto código: {}", produto.getPreco(), produto.getCodigo());
          
            throw new IllegalArgumentException("Preco do produto invalido");
        }
        if (produto.getQuantidadeEstoque() <= 0) {
            log.warn("Falha no cadastro: Estoque {} é inválido para o produto código: {}", produto.getQuantidadeEstoque(), produto.getCodigo());
          
            throw new IllegalArgumentException("Quantidade em estoque do produto invalida");
        }

       
        Optional<Produto> produtoExistente = produtoRepository.findByCodigo(produto.getCodigo());
        if (produtoExistente.isPresent()) {
                log.warn("Falha no cadastro: Produto com código {} já existe no repositório", produto.getCodigo());
              
                throw new IllegalArgumentException("Produto com codigo " + produto.getCodigo() + " ja existe");
        }
        
        Produto novoProduto = produtoRepository.save(produto);
        log.info("Produto cadastrado com sucesso. ID: {}, Código: {}", novoProduto.getId(), novoProduto.getCodigo());
      
        return novoProduto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Produto> listarTodos() {
        log.debug("Solicitando listagem completa de produtos");
        
        List<Produto> produtos = produtoRepository.findAll();
        
        log.info("Listagem de produtos total de registros encontrados: {}", produtos.size());
       
        return produtos;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Produto> buscarPorId(Long id) {
        if (id == null || id <= 0) {
            log.warn("Tentativa de busca com ID inválido: {}", id);
            
            throw new IllegalArgumentException("ID do produto é inválido para busca");
        }

        log.debug("Buscando produto por ID: {}", id);
        Optional<Produto> produtoOpt = produtoRepository.findById(id);

        if (produtoOpt.isPresent()) {
            log.info("Produto encontrado com sucesso. ID: {}", id);
           
        } else {
            log.warn("Busca realizada mas produto não foi encontrado. ID: {}", id);
          
        }

        return produtoOpt;
    }

    @Override
    @Transactional
    public Optional<Produto> atualizar(Long id, Produto produtoAtualizado) {
        if (id == null || id <= 0) {
            log.warn("Tentativa de atualização com ID inválido: {}", id);
          
            throw new IllegalArgumentException("ID inválido para atualização");
        }

        if (produtoAtualizado == null) {
            log.warn("Tentativa de atualização com dados nulos para o ID: {}", id);
          
            throw new IllegalArgumentException("Dados de atualização não podem ser nulos");
        }

        log.debug("Iniciando atualização do produto ID: {}", id);

        Optional<Produto> produtoAtualizadoOpt = produtoRepository.findById(id).map(produto -> {
            produto.setCodigo(produtoAtualizado.getCodigo());
            produto.setDescricao(produtoAtualizado.getDescricao());
            produto.setPreco(produtoAtualizado.getPreco());
            produto.setQuantidadeEstoque(produtoAtualizado.getQuantidadeEstoque());
            return produtoRepository.save(produto);
        });

        if (produtoAtualizadoOpt.isPresent()) {
            log.info("Produto ID: {} atualizado com sucesso no repositório", id);
          
        } else {
            log.warn("Falha na atualização: Produto ID: {} não foi encontrado", id);
          
        }

        return produtoAtualizadoOpt;
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (id == null || id <= 0) {
            log.warn("Tentativa de exclusão de produto com ID inválido: {}", id);
          
            throw new IllegalArgumentException("ID do produto inválido para exclusão");
        }

        if (!produtoRepository.existsById(id)) {
            log.warn("Tentativa de exclusão de produto inexistente. ID: {}", id);
           
            throw new IllegalArgumentException("Produto não encontrado para o ID: " + id);
        }

        produtoRepository.deleteById(id);
      
    }

   
}