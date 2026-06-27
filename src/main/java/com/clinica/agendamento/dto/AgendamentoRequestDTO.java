package com.clinica.agendamento.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
 
import java.time.LocalDateTime;
 
// DTO de requisição para criar um novo agendamento, aqui definimos o que o usuario pode enviar
// a requisição deve conter o ID do paciente, ID do profissional e a data e hora do agendamento
// a Anotação @Future garante que o usuario não envie uma data e hora no passado, mas ainda assim validei no service para garantir
// visto que o @Future sozinha não cobre o caso de agendamento no mesmo instante que o usuario envia a requisição, visto que 
// o @Future considera o momento da validação e não o momento do envio da requisição

public record AgendamentoRequestDTO(
 
        @NotNull(message = "O id do paciente e obrigatorio")
        Long pacienteId,
 
        @NotNull(message = "O id do profissional e obrigatorio")
        Long profissionalId,
 
        @NotNull(message = "A data e hora sao obrigatorias")
        @Future(message = "A data e hora devem estar no futuro")
        LocalDateTime dataHora,
 
        @NotBlank(message = "O tipo de atendimento e obrigatorio")
        String tipoAtendimento
) {
}