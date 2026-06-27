package com.clinica.agendamento.dto;
 
import java.time.LocalDateTime;
// estrutura padronizada de resposta de erro da API.
// garante que todo erro retorne no mesmo formato JSON, facilitando o consumo pelo cliente (frontend, Postman, etc).

public record ErroResponseDTO(
        LocalDateTime timestamp,
        int status,
        String erro,
        String mensagem
) {
}
 