package com.fatec.vendas.servico;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fatec.vendas.model.Produto;

@Repository
public interface IProdutoRepository extends JpaRepository<Produto, Long> {
}