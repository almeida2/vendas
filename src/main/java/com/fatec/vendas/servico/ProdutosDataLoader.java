package com.fatec.vendas.servico;

import com.fatec.vendas.model.Produto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProdutosDataLoader implements CommandLineRunner {

    private final IProdutoRepository produtoRepository;

    public ProdutosDataLoader(IProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (produtoRepository.count() > 0) {
            return;
        }

        List<Produto> produtos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new ClassPathResource("produtos.csv").getInputStream(), StandardCharsets.UTF_8))) {
            String line = reader.readLine();
            if (line == null) {
                return;
            }

            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) {
                    continue;
                }

                String[] fields = line.split(",", -1);
                if (fields.length != 5) {
                    throw new IllegalArgumentException(
                            "Linha " + lineNumber + " inválida em produtos.csv: esperados 5 campos.");
                }

                Produto produto = new Produto();
                produto.setCodigo(fields[1].trim());
                produto.setDescricao(fields[2].trim());
                produto.setPreco(Double.parseDouble(fields[3].trim()));
                produto.setQuantidadeEstoque(Integer.parseInt(fields[4].trim()));
                produtos.add(produto);
            }
        }

        if (!produtos.isEmpty()) {
            produtoRepository.saveAll(produtos);
        }
    }
}
