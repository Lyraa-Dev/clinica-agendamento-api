package com.clinica.agendamento.repository;

import com.clinica.agendamento.model.Agendamento;
import com.clinica.agendamento.model.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

// Repositório de Agendamento,alem do CRUD, aqui defini as consultas que sustentam a regra de negocio proposta pelo teste 

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    
// Regra: um profissional não pode ter dois agendamentos no mesmo horário
// Verifica se já existe agendamento para o profissional naquela data e hora que não esteja com o Status cancelado 
// visto que um agendamento cancelado disponibilizaria novamente o horario
// Retorna boolean para ser usado no service, antes da criação de um agendamento 

    boolean existsByProfissionalIdAndDataHoraAndStatusNot(
            Long profissionalId,
            LocalDateTime dataHora,
            StatusAgendamento status);

    // agora criamos o filtro de listagem de agendamentos por paciente, profissional e status, com qualquer combinação
    // Usei JPQL com os parametros opcionais, para que o filtro seja flexivel quando o parametro vem null
    // quando o parametro é null, essa condição é ignorada o ":x IS NULL OR ..." vira sempre verdadeiro
    // dessa forma um unico metodo cobre todas as combinações de filtro de nossa regra de negocio

    @Query("""
            SELECT a FROM Agendamento a
            WHERE (:pacienteId IS NULL OR a.paciente.id = :pacienteId)
              AND (:profissionalId IS NULL OR a.profissional.id = :profissionalId)
              AND (:status IS NULL OR a.status = :status)
            ORDER BY a.dataHora ASC
            """)
    List<Agendamento> buscarComFiltros(
            @Param("pacienteId") Long pacienteId,
            @Param("profissionalId") Long profissionalId,
            @Param("status") StatusAgendamento status);
}
