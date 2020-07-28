package com.genesis.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Entreprise {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String adresse;

    @Column
    @ElementCollection(targetClass=String.class)
    private List<String> autreAdresses;

    @Column(nullable = false)
    private String numeroTVA;

    @ManyToMany
    @Column
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Contact> contacts;

}
