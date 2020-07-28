package com.genesis.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String adresse;

    @Column(nullable = false)
    private String lastName;

    @OneToMany
    private List<Entreprise> entreprise;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatutContact statutContact;

    @Column
    private String numeroTVA;


}
