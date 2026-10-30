package com.fatec.vendas.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

import com.fatec.vendas.util.BuilderProduto;
import com.fatec.vendas.model.Produto;
import com.fatec.vendas.servico.IProdutoRepository;
import com.fatec.vendas.util.JsonTestUtils;

/**
 * Testes de Integração para a camada de controle ProdutoController
 * (/api/v1/produtos).
 * Executados em contexto real do Spring Boot sem o uso de mocks.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
public class TICManterProdutoCRUDTests {

    private static final String ENDPOINT = "/api/v1/produtos";

    @Autowired
    private RestTestClient restClient;

    @Autowired
    private IProdutoRepository produtoRepository;

    @BeforeEach
    void setUp() {
        // Garante isolamento antes de cada teste limpando a base de dados
        produtoRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        // Garante que o banco seja resetado após a execução
        produtoRepository.deleteAll();
    }

    @Test
    @DisplayName("POST /api/v1/produtos - Deve cadastrar produto com sucesso a partir de arquivo JSON")
    void ct01_quandoSalvarProdutoValido_deveRetornarStatus201ECorpoPersistido() {
        // Dado (Given): Carga do JSON de entrada e configuração da requisição HTTP
        String jsonValido = JsonTestUtils.carregarJson("json/produto-valido-input.json");

        // Quando (When): Chamada do endpoint HTTP sob teste (POST /api/v1/produtos)
        Produto produtoCriado = restClient.post()
                .uri(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonValido)
                .exchange()
                // Então (Then): Validação do status 201 Created, cabeçalho Location e corpo
                .expectStatus().isCreated()
                .expectHeader().value("Location", location -> {
                    assertNotNull(location, "O cabeçalho Location não deve ser nulo");
                    assertTrue(location.contains(ENDPOINT),
                            "O cabeçalho Location deve apontar para o recurso criado");
                })
                .expectBody(Produto.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(produtoCriado, "O corpo da resposta não deve ser nulo");
        assertNotNull(produtoCriado.getId(), "O ID do produto deve ser gerado pelo banco");
        assertEquals("PROD001", produtoCriado.getCodigo());
        assertEquals("Teclado Mecanico", produtoCriado.getDescricao());
        assertEquals(250.00, produtoCriado.getPreco());
        assertEquals(5, produtoCriado.getQuantidadeEstoque());

        // Confirmação da gravação no banco via repositório real
        Optional<Produto> produtoNoBanco = produtoRepository.findById(produtoCriado.getId());
        assertTrue(produtoNoBanco.isPresent(), "O produto deve estar persistido no banco de dados real");
        assertEquals("PROD001", produtoNoBanco.get().getCodigo());
    }

    @Test
    @DisplayName("POST /api/v1/produtos - Deve validar restrições ao tentar cadastrar produto inválido")
    void ct02_quandoSalvarProdutoInvalido_deveRetornarStatusErro() {
        // Dado (Given): Carga do JSON de entrada com dados inválidos
        String jsonInvalido = JsonTestUtils.carregarJson("json/produto-invalido-input.json");

        // Quando (When): Chamada do endpoint HTTP sob teste (POST /api/v1/produtos)
        restClient.post()
                .uri(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonInvalido)
                .exchange()
                // Então (Then): Validação de recusa da requisição (4xx) e integridade do banco
                .expectStatus().is4xxClientError();

        assertEquals(0, produtoRepository.count(), "Nenhum produto inválido deve ser persistido no banco");
    }

    @Test
    @DisplayName("GET /api/v1/produtos/{id} - Deve retornar 200 OK com os detalhes do produto existente")
    void ct03_quandoBuscarPorIdExistente_deveRetornarStatus200EProduto() {
        // Dado (Given): Preparação do registro pré-existente no banco de dados real
        Produto produto = BuilderProduto.umProduto().build();
        Produto produtoSalvo = produtoRepository.save(produto);

        // Quando (When): Chamada do endpoint HTTP sob teste (GET /api/v1/produtos/{id})
        restClient.get()
                .uri(ENDPOINT + "/{id}", produtoSalvo.getId())
                .exchange()
                // Então (Then): Validação do status 200 OK e integridade dos dados retornados
                .expectStatus().isOk()
                .expectBody(Produto.class)
                .value(produtoRetornado -> {
                    assertNotNull(produtoRetornado, "O produto retornado não deve ser nulo");
                    assertEquals(produtoSalvo.getId(), produtoRetornado.getId());
                    assertEquals("PROD001", produtoRetornado.getCodigo());
                    assertEquals("Teclado Mecanico", produtoRetornado.getDescricao());
                    assertEquals(250.00, produtoRetornado.getPreco());
                    assertEquals(5, produtoRetornado.getQuantidadeEstoque());
                });
    }

    @Test
    @DisplayName("GET /api/v1/produtos/{id} - Deve retornar 404 Not Found para ID inexistente")
    void ct04_quandoBuscarPorIdInexistente_deveRetornarStatus404() {
        // Dado (Given): Configuração de um ID que não existe na base de dados
        Long idInexistente = 9999L;

        // Quando (When): Chamada do endpoint HTTP sob teste (GET /api/v1/produtos/{id})
        restClient.get()
                .uri(ENDPOINT + "/{id}", idInexistente)
                .exchange()
                // Então (Then): Validação de resposta 404 Not Found
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("GET /api/v1/produtos - Deve retornar 200 OK com a lista contendo todos os registros")
    void ct05_quandoListarTodos_deveRetornarStatus200EListaDeProdutos() {
        // Dado (Given): Persistência de múltiplos registros reais no banco de dados
        Produto p1 = BuilderProduto.umProduto().comCodigo("PROD001").comDescricao("Teclado").build();
        Produto p2 = BuilderProduto.umProduto().comCodigo("PROD002").comDescricao("Mouse").comPreco(150.00).build();
        produtoRepository.save(p1);
        produtoRepository.save(p2);

        // Quando (When): Chamada do endpoint HTTP sob teste (GET /api/v1/produtos)
        restClient.get()
                .uri(ENDPOINT)
                .exchange()
                // Então (Then): Validação do status 200 OK e quantidade de itens retornados
                .expectStatus().isOk()
                .expectBody(Produto[].class)
                .value(produtos -> {
                    assertNotNull(produtos, "A lista de produtos não deve ser nula");
                    assertEquals(2, produtos.length, "Devem ser retornados 2 produtos");
                });
    }

    @Test
    @DisplayName("PUT /api/v1/produtos/{id} - Deve atualizar produto existente com sucesso e retornar 200 OK")
    void ct06_quandoAtualizarProdutoExistente_deveRetornarStatus200EAtualizarNoBanco() {
        // Dado (Given): Persistência de produto inicial no banco e carga do JSON de
        // atualização
        Produto produtoOriginal = BuilderProduto.umProduto().build();
        Produto produtoSalvo = produtoRepository.save(produtoOriginal);

        String jsonAtualizacao = JsonTestUtils.carregarJson("json/produto-atualizado-input.json");

        // Quando (When): Chamada do endpoint HTTP sob teste (PUT /api/v1/produtos/{id})
        restClient.put()
                .uri(ENDPOINT + "/{id}", produtoSalvo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonAtualizacao)
                .exchange()
                // Então (Then): Validação do status 200 OK e confirmação dos dados
                .expectStatus().isOk()
                .expectBody(Produto.class)
                .value(produtoAtualizado -> {
                    assertNotNull(produtoAtualizado, "O produto atualizado retornado não deve ser nulo");
                    assertEquals(produtoSalvo.getId(), produtoAtualizado.getId());
                    assertEquals("Teclado Mecanico RGB", produtoAtualizado.getDescricao());
                    assertEquals(300.00, produtoAtualizado.getPreco());
                    assertEquals(10, produtoAtualizado.getQuantidadeEstoque());
                });

        // Confirmação no repositório real
        Produto noBanco = produtoRepository.findById(produtoSalvo.getId()).orElseThrow();
        assertEquals("Teclado Mecanico RGB", noBanco.getDescricao());
        assertEquals(300.00, noBanco.getPreco());
        assertEquals(10, noBanco.getQuantidadeEstoque());
    }

    @Test
    @DisplayName("DELETE /api/v1/produtos/{id} - Deve excluir produto existente e retornar 204 No Content")
    void ct07_quandoDeletarProdutoExistente_deveRetornarStatus204ERemoverDoBanco() {
        // Dado (Given): Persistência de um produto no banco real para exclusão
        Produto produto = BuilderProduto.umProduto().build();
        Produto produtoSalvo = produtoRepository.save(produto);
        Long id = produtoSalvo.getId();
        assertTrue(produtoRepository.existsById(id), "Produto deve existir antes da exclusão");

        // Quando (When): Chamada do endpoint HTTP sob teste (DELETE
        // /api/v1/produtos/{id})
        restClient.delete()
                .uri(ENDPOINT + "/{id}", id)
                .exchange()
                // Então (Then): Validação do status 204 No Content e remoção no banco real
                .expectStatus().isNoContent();

        assertFalse(produtoRepository.existsById(id), "Produto não deve mais existir no banco após exclusão");
        assertEquals(0, produtoRepository.count());
    }

    @Test
    @DisplayName("DELETE /api/v1/produtos/{id} - Deve retornar 404 Not Found com mensagem ao tentar excluir ID inexistente")
    void ct08_quandoDeletarProdutoInexistente_deveRetornarStatus404EMensagemNoCorpo() {
        // Dado (Given): ID inexistente gera IllegalArgumentException com a mensagem
        // "não encontrado"
        Long idInexistente = 9999L;

        // Quando (When): Chamada do endpoint HTTP sob teste (DELETE
        // /api/v1/produtos/{id})
        restClient.delete()
                .uri(ENDPOINT + "/{id}", idInexistente)
                .exchange()
                // Então (Then): Validação do status 404 Not Found e verificação da mensagem no
                // corpo
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .value(corpo -> {
                    assertNotNull(corpo, "O corpo da resposta de erro não deve ser nulo");
                    assertTrue(corpo.contains("não encontrado"),
                            "A mensagem no corpo deve conter 'não encontrado'");
                });
    }
}
