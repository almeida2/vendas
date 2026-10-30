package com.fatec.vendas.servico;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fatec.vendas.model.Produto;

public interface IProdutoRepository extends JpaRepository<Produto, Long> {
    java.util.Optional<Produto> findByCodigo(String codigo);
}