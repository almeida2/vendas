package com.fatec.vendas.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fatec.vendas.model.Produto;
import com.fatec.vendas.servico.IProdutoService;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/produtos")
public class ProdutoController {

    private final IProdutoService produtoService;
    Logger logger = LogManager.getLogger(this.getClass());

    public ProdutoController(IProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public ResponseEntity<List<Produto>> listarTodos() {
        logger.info(">>>>>> controller listar todos os produtos iniciado...");
        return ResponseEntity.ok(produtoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Produto> salvar(@Valid @RequestBody Produto produto) {
        try {
            logger.info(">>>>>> controller cadastro do produto iniciado...");
            Produto produtoSalvo = produtoService.cadastrarProduto(produto);
            var localizacao = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(produtoSalvo.getId())
                    .toUri();
            return ResponseEntity.created(localizacao).body(produtoSalvo);
        } catch (IllegalArgumentException ex) {
            logger.info(">>>>>> controller cadastro do produto erro: " + ex.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
        
       
    }

    /**
     * @param id
     * @return ResponseEntity com status HTTP apropriado:
     *         - HTTP 204 (No Content) se a exclusão for bem-sucedida
     *         - HTTP 404 (Not Found) se o produto não for encontrado
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        try {
            logger.info(">>>>>> controller exclusão do produto iniciada ... id= " + id);
            produtoService.deletar(id);
            return ResponseEntity.noContent().build(); // HTTP 204
        } catch (IllegalArgumentException | InvalidDataAccessApiUsageException ex) {
            logger.info(">>>>>> controller exclusão do produto erro: " + ex.getMessage());
            String mensagem = ex.getMessage();
            if (ex.getCause() != null && ex.getCause().getMessage() != null) {
                mensagem = ex.getCause().getMessage();
            }
            if (mensagem != null && mensagem.contains("não encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensagem); // HTTP 404
            }
            return ResponseEntity.badRequest().body(mensagem); // HTTP 400
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @Valid @RequestBody Produto produto) {
        logger.info(">>>>>> controller atualizar produto iniciado...");
        return produtoService.atualizar(id, produto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
