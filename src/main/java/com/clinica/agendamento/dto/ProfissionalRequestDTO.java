package com.clinica.agendamento.dto;
 
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
// DTO de requisição para criar um novo profissional, aqui definimos o que o usuario pode enviar

public record ProfissionalRequestDTO(
 
        @NotBlank(message = "O nome e obrigatorio")
        @Size(max = 120, message = "O nome deve ter no maximo 120 caracteres")
        String nome,
 
        @NotBlank(message = "A especialidade e obrigatoria")
        @Size(max = 80, message = "A especialidade deve ter no maximo 80 caracteres")
        String especialidade,
 
        @NotBlank(message = "O registro (CRM/CRO) e obrigatorio")
        @Size(max = 30, message = "O registro deve ter no maximo 30 caracteres")
        String registro
) {
}