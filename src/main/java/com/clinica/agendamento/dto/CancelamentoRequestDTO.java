package com.clinica.agendamento.dto;
 
import jakarta.validation.constraints.NotBlank;

// DTO de requisição para cancelar um agendamento, aqui definimos o que o usuario pode enviar
// o motivo do cancelamentoé obrigatorio

public record CancelamentoRequestDTO(
 
        @NotBlank(message = "O motivo do cancelamento e obrigatorio")
        String motivo
) {
}