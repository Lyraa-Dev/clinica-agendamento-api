package com.clinica.agendamento.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// DTO de requisição para criar um novo paciente, aqui definimos o que o usuario pode enviar
// Usei 'record' que pertence ao Java 14+ apesar de ser uma feature que não tem no Java 8, é uma forma que eu sei para criar uma classe imutavel
// as validações criadas garantem que o usuario não envie dados invalidos, como cpf com letras ou telefone com menos de 8 digitos
public record PacienteRequestDTO(

    @NotBlank(message = "O nome é obrigatorio")
        @Size(max = 120, message = "O nome deve ter no maximo 120 caracteres")
        String nome,
 
        @NotBlank(message = "O CPF é obrigatorio")
        @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 digitos numericos")
        String cpf,
 
        @Size(max = 20, message = "O telefone deve ter no maximo 20 caracteres")
        String telefone
) {
}
