package org.generation.blogpessoal.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.generation.blogpessoal.model.Usuario;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UsuarioRepositoryTest {
    
    private @Autowired UsuarioRepository usuarioRepository;

    @BeforeAll
    void start() {
        usuarioRepository.deleteAll();
        usuarioRepository.save(new Usuario(0L, "Luciana Silva", "Lucy", "Lu123456", "http://imagem.jpg"));
        usuarioRepository.save(new Usuario(0L, "Maria Silva", "Maria@hotmail.com", "mmm123", "http://imagem.jpg"));
        usuarioRepository.save(new Usuario(0L, "Samuel Silva", "Sa", "abcdefg12", "http://imagem.jpg"));
    }

    @Test
    public void returnOneUser() {

        Optional<Usuario> usuario = usuarioRepository.findByUsuario("Maria@hotmail.com");
        assertTrue(usuario.get().getUsuario().equals("Maria@hotmail.com"));
    }

    @Test
    public void returnThreeUsers() {

        List<Usuario> listaUsuario = usuarioRepository.findAllByNomeContainingIgnoreCase("Silva");
        assertEquals(3, listaUsuario.size());
        assertTrue(listaUsuario.get(0).getNome().equals("Luciana Silva"));
        assertTrue(listaUsuario.get(1).getNome().equals("Maria Silva"));
        assertTrue(listaUsuario.get(2).getNome().equals("Samuel Silva"));
    }

    @Test
    public void returnNameListSizeWithM() {

        List<Usuario> listaUsuario = usuarioRepository.findAllByNomeContainingIgnoreCase("m");
        assertEquals(2, listaUsuario.size());
    }
}
