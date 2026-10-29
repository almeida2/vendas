package com.fatec.vendas.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

public class JsonTestUtils {

    private JsonTestUtils() {
    }

    public static String carregarJson(String caminho) {
        try {
            ClassPathResource resource = new ClassPathResource(caminho);
            return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler o arquivo JSON: " + caminho, e);
        }
    }
}

