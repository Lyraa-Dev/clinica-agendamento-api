package com.clinica.agendamento.model;

import jakarta.persistence.*;

//Entidade Profissional, representa o profissional de saude que realizará o antendimento 

@Entity
@Table(name = "profissional")
public class Profissional {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, length = 80)
    private String especialidade;

    public Profissional() {
    }

    public Profissional(String nome, String especialidade) {
        this.nome = nome;
        this.especialidade = especialidade;
    }

    public Long getId() {
        return id;
    
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

}    

