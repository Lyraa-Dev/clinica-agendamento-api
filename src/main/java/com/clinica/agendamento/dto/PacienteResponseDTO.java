package com.clinica.agendamento.dto;

import com.clinica.agendamento.model.Paciente;

// DTO de resposta para o paciente, usado para enviar informações do paciente em respostas da API
// o metodo fromEntity é um util para converter a entidade Paciente em PacienteResponseDTO
// facilitando a transformação de dados entre camadas da aplicação
public record PacienteResponseDTO(
        Long id,
        String nome,
        String cpf,
        String telefone
) {
    public static PacienteResponseDTO fromEntity(Paciente paciente) {
        return new PacienteResponseDTO(
                paciente.getId(),
                paciente.getNome(),
                paciente.getCpf(),
                paciente.getTelefone()
        );
    }
}
