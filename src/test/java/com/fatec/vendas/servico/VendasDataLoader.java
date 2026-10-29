package com.fatec.vendas.servico;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fatec.vendas.model.Venda;

@Component
public class VendasDataLoader implements CommandLineRunner {

    @Autowired
    private IVendaRepository vendaRepository;

    @Override
    public void run(String... args) throws Exception {
        if (vendaRepository.count() == 0) {
            loadVendasData();
        }
    }

    private void loadVendasData() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource("dataset-vendas.csv").getInputStream()))) {

            String line;
            boolean isFirstLine = true;
            List<Venda> vendasList = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Ignora o cabeçalho
                    continue;
                }

                String[] data = line.split(",");
                if (data.length >= 4) {
                    Venda venda = new Venda(data[1]); // cpf
                    venda.setDataVenda(LocalDate.parse(data[2], formatter));
                    venda.setTotalVenda(Double.parseDouble(data[3]));
                    vendasList.add(venda);
                }
            }

            vendaRepository.saveAll(vendasList);
            System.out.println(
                    ">>>>>> Dataset de vendas carregado com sucesso! Total de registros: " + vendasList.size());

        } catch (Exception e) {
            System.err.println(">>>>>> Erro ao carregar o arquivo dataset-vendas.csv: " + e.getMessage());
        }
    }
}
