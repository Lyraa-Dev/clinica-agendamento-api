package com.clinica.agendamento.dto;
 
import com.clinica.agendamento.model.Agendamento;
import com.clinica.agendamento.model.StatusAgendamento;
 
import java.time.LocalDateTime;

// DTO de resposta para retornar os dados do agendamento, aqui definimos o que o usuario vai receber
// recebe dados resumidos do paciente, do profissional para a resposta ser legivel, sem expor as entidades completas no banco

public record AgendamentoResponseDTO(
        Long id,
        Long pacienteId,
        String pacienteNome,
        Long profissionalId,
        String profissionalNome,
        LocalDateTime dataHora,
        String tipoAtendimento,
        StatusAgendamento status,
        String motivoCancelamento
) {
    public static AgendamentoResponseDTO fromEntity(Agendamento a) {
        return new AgendamentoResponseDTO(
                a.getId(),
                a.getPaciente().getId(),
                a.getPaciente().getNome(),
                a.getProfissional().getId(),
                a.getProfissional().getNome(),
                a.getDataHora(),
                a.getTipoAtendimento(),
                a.getStatus(),
                a.getMotivoCancelamento()
        );
    }
}