package com.clinica.agendamento.dto;

import com.clinica.agendamento.model.Profissional;

// DTO de resposta para o profissional, usado para enviar informações do profissional em respostas da API
    
public record ProfissionalResponseDTO(
        Long id,
        String nome,
        String especialidade,
        String registro
) {
    public static ProfissionalResponseDTO fromEntity(Profissional profissional) {
        return new ProfissionalResponseDTO(
                profissional.getId(),
                profissional.getNome(),
                profissional.getEspecialidade(),
                profissional.getRegistro()
        );
    }
}