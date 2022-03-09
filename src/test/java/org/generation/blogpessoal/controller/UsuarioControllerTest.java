package org.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.generation.blogpessoal.model.Usuario;
import org.generation.blogpessoal.repository.UsuarioRepository;
import org.generation.blogpessoal.service.UsuarioService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UsuarioControllerTest {

    private @Autowired TestRestTemplate testRestTemplate;
    private @Autowired UsuarioService service;
    private @Autowired UsuarioRepository repository;
    
    @BeforeAll
    void start () {
        repository.deleteAll();
    }

    @Test
    @Order(1)
    void createUser () {
        HttpEntity<Usuario> request = new HttpEntity<Usuario>(new Usuario(0L, "Luciana", "Lu02", "Lu123456", "http://imagemLu.jpg"));

        ResponseEntity<Usuario> response = testRestTemplate
            .exchange("/usuarios/cadastrar", HttpMethod.POST, request, Usuario.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(request.getBody().getNome(), response.getBody().getNome());
        assertEquals(request.getBody().getUsuario(), response.getBody().getUsuario());
        // assertEquals(request.getBody().getSenha(), response.getBody().getSenha());
        assertEquals(request.getBody().getFoto(), response.getBody().getFoto());
    }

    @Test
    @Order(2)
    void denyDuplicatedUsers() {
        
        service.cadastrarUsuario(new Usuario(0L, "Marcelo", "marcelo@gmail.com", "senha1234", "http://imagem.jpg"));
        
        HttpEntity<Usuario> request = new HttpEntity<Usuario>(new Usuario(0L, "Marcelo", "marcelo@gmail.com", "senha1234", "http://imagem.jpg"));

        ResponseEntity<Usuario> response = testRestTemplate
            .exchange("/usuarios/cadastrar", HttpMethod.POST, request, Usuario.class);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(3)
    void alterUser() {

        Optional<Usuario> userCreate = service.cadastrarUsuario(new Usuario(0L, "Pedro", "pedro@gmail.com", "senhapedro", "http://link.jpg"));

        Usuario userUpdate = new Usuario(userCreate.get().getId(), "Pedro", "pedro@gmail.com", "senhapedro", "http://linkIMAGEM.png");

        HttpEntity<Usuario> request = new HttpEntity<Usuario>(userUpdate);

        ResponseEntity<Usuario> response = testRestTemplate
            .withBasicAuth("root", "root")
            .exchange("/usuarios/atualizar", HttpMethod.PUT, request, Usuario.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(request.getBody().getNome(), response.getBody().getNome());
        assertEquals(request.getBody().getUsuario(), response.getBody().getUsuario());
        assertEquals(request.getBody().getFoto(), response.getBody().getFoto());
    }

    @Test
    @Order(4)
    void showAllUsers() {

        service.cadastrarUsuario(new Usuario (0L, "Samuel Silva", "Sa", "abcdefg12", "http://imagem.jpg"));
        service.cadastrarUsuario(new Usuario (0L, "Maria Silva", "Maria@hotmail.com", "mmm123", "http://imagem.jpg"));

        ResponseEntity<String> response = testRestTemplate
        .withBasicAuth("root", "root")
        .exchange("/usuarios/all", HttpMethod.GET, null, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
