package org.generation.blogpessoal.repository;

import java.util.List;

import org.generation.blogpessoal.model.Tema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//JpaReposutory -> permite a persistencia de dados, ou seja, conseguimos fazer todo o CRUD através dessa biblioteca

@Repository
public interface TemaRepository extends JpaRepository<Tema, Integer> {
    
    public List<Tema> findAllByDescricaoContainingIgnoreCase(String descricao);
}
