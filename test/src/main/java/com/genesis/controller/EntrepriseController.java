package com.genesis.controller;

import com.genesis.exceptions.NotFoundException;
import com.genesis.model.Entreprise;
import com.genesis.repository.EntrepriseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/entreprises")
public class EntrepriseController {
    @Autowired
    private EntrepriseRepository entrepriseRepository;

    @GetMapping
    public Iterable findAll() {
        return this.entrepriseRepository.findAll();
    }

    @GetMapping("/{id}")
    public Entreprise findOne(@PathVariable final Long id) {
        return this.entrepriseRepository.findById(id).orElseThrow(() -> new NotFoundException("Id"+ id+ "cannot be found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Entreprise create(@RequestBody final Entreprise entreprise) {
        return this.entrepriseRepository.save(entreprise);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable final Long id) {
        this.entrepriseRepository.findById(id).orElseThrow(() -> new NotFoundException("Id"+ id+ "cannot be found"));
        this.entrepriseRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public Entreprise updateEntreprise(@RequestBody final Entreprise entreprise, @PathVariable final Long id) {
        if ( entreprise.getId() != id ) {
            throw new NotFoundException("Id"+ id+ "cannot be found");
        }
        this.entrepriseRepository.findById(id).orElseThrow(() -> new NotFoundException("Id"+ id+ "cannot be found"));
        return this.entrepriseRepository.save(entreprise);
    }
}
