package org.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

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
import org.springframework.web.server.ResponseStatusException;

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
    public ResponseEntity<Tema> getByIdTema(@PathVariable (value = "id_tema") Long idTema) {
        return repository.findById(idTema).map(resp -> ResponseEntity.ok(resp))
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    //findByDescricaoPostagem
    @GetMapping("/descricao/{descricao}")
    public ResponseEntity<List<Tema>> getByDescriptionPost (@PathVariable (value = "descricao") String descricao) {
        return ResponseEntity.ok(repository.findAllByDescricaoContainingIgnoreCase(descricao));
    }

    //postTema
    @PostMapping
    public ResponseEntity<Tema> post(@Valid @RequestBody Tema tema) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(tema));
    }

    //putTema
    @PutMapping
    public ResponseEntity<Tema> put(@Valid @RequestBody Tema tema) {
        return repository.findById(tema.getId()).map(resp -> ResponseEntity.status(HttpStatus.CREATED)
            .body(repository.save(tema))).orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    //deleteTema
    @DeleteMapping("/{id_tema}")
    private void delete(@PathVariable Long id_tema) {
        Optional<Tema> tema = repository.findById(id_tema);

        if (tema.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tema não encontrado.", null);

        repository.deleteById(id_tema);
    }
}   
