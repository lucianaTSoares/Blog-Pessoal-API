package org.generation.blogpessoal.controller;

import java.util.List;

import org.generation.blogpessoal.model.Tema;
import org.generation.blogpessoal.repository.TemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/temas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TemaController {
    
    // injecao de dependencia
    private @Autowired TemaRepository repository;

    //findAllTema
    @GetMapping
    public ResponseEntity<List<Tema>> getAllTema() {
        return ResponseEntity.ok(repository.findAll());
    }

    //findByIDTema
    @GetMapping("/{id_tema}")
    public ResponseEntity<Tema> getByIdTema(@PathVariable (value = "id_tema") Integer idTema) {
        return repository.findById(idTema).map(resp -> ResponseEntity.ok(resp))
        .orElse(ResponseEntity.notFound().build());
    }

    //findByDescricaoPostagem
    @GetMapping("/nome/{texto}")
    public ResponseEntity<List<Tema>> getByDescriptionPost (@PathVariable (value = "texto") String texto) {
        return ResponseEntity.ok(repository.findAllByDescricaoContainingIgnoreCase(texto));
    }

    //postTema
    @PostMapping
    public ResponseEntity<Tema> post(@RequestBody Tema temas) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(temas));
    }

    //putTema
    @PutMapping
    public ResponseEntity<Tema> put(@RequestBody Tema temas) {
        return ResponseEntity.status(HttpStatus.OK).body(repository.save(temas));
    }

    //deleteTema
    @DeleteMapping("/{id_tema}")
    private void delete(@PathVariable Integer id_tema) {
        repository.deleteById(id_tema);
    }
}   
