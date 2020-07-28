package com.genesis.controller;

import com.genesis.exceptions.BadRequestException;
import com.genesis.exceptions.NotFoundException;
import com.genesis.model.Contact;
import com.genesis.model.StatutContact;
import com.genesis.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contacts")
@Slf4j
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    @GetMapping
    public Iterable findAll() {
        return this.contactRepository.findAll();
    }

    @GetMapping("/{id}")
    public Contact findOne(@PathVariable final Long id) {
        return this.contactRepository.findById(id).orElseThrow(() -> new NotFoundException("Id"+ id+ "cannot be found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Contact create(@RequestBody final Contact contact) {
        if( contact.getStatutContact().equals(StatutContact.FREELANCE) && (contact.getNumeroTVA() == null) ){
            throw new BadRequestException("VAT number cannot be NULL when employee status is " + contact.getStatutContact());
        }
        return this.contactRepository.save(contact);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable final Long id) {
        this.contactRepository.findById(id).orElseThrow(() -> new NotFoundException("Id "+ id + " cannot be found"));
        this.contactRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public Contact updateContact(@RequestBody final Contact contact, @PathVariable final Long id) {
        if (contact.getId() != id) {
            throw new NotFoundException("Id "+ id + " cannot be found");
        }
        this.contactRepository.findById(id).orElseThrow(() -> new NotFoundException("Id "+ id + " cannot be found"));
        return this.contactRepository.save(contact);
    }
}
