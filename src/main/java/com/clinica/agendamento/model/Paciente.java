package com.clinica.agendamento.model;

import jakarta.persistence.*;
//  Entidade paciente representa um paciente no sistema de agendamento da clínica mapeada para a tabela "paciente" no banco de dados

@Entity
@Table(name = "paciente")
public class Paciente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;
    
    @Column(nullable = false, length = 14, unique = true)
    private String cpf;

    @Column(length = 20)
    private String telefone;

    public Paciente() {
    }

    public Paciente(String nome, String cpf, String telefone) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    
}
