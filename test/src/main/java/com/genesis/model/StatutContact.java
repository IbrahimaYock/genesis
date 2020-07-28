package com.genesis.model;


import lombok.Getter;

public enum StatutContact {
    EMPLOYE("Employe"),
    FREELANCE("Freelance");

    @Getter
    private String label;

    StatutContact(final String label){
        this.label = label;
    }

}
