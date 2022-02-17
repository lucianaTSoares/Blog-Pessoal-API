package org.generation.blogpessoal.controller;

import java.util.List;

import org.generation.blogpessoal.model.Post;
import org.generation.blogpessoal.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/postagens")
@CrossOrigin("*")
public class PostController {
    
    @Autowired
    private PostRepository repository;

    // método GetAll que faz a requisição e retorna todos os posts
    @GetMapping("/todas")
    public ResponseEntity<List<Post>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }
}
