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

    // Decidi criar tambem o campo de registro (CRM, CRO, etc) para identificar o profissional de forma unica
    // para seguir a mesma regra de negocio do paciente, que não pode ter CPF duplicado, o profissional não pode ter registro
    //  duplicado
    @Column(nullable = false, length = 30, unique = true)
    private String registro;
 
    public Profissional() {
    }
 
    public Profissional(String nome, String especialidade, String registro) {
        this.nome = nome;
        this.especialidade = especialidade;
        this.registro = registro;
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
 
    public String getRegistro() {
        return registro;
    }
 
    public void setRegistro(String registro) {
        this.registro = registro;
    }
}