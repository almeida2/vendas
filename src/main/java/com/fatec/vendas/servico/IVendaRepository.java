package com.fatec.vendas.servico;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.fatec.vendas.model.Venda;
public interface IVendaRepository extends JpaRepository<Venda, Long> {
    List<Venda> findByCpf(String cpf);
}
