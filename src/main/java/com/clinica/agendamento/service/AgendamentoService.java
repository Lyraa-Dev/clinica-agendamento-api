package com.clinica.agendamento.service;
 
import com.clinica.agendamento.dto.AgendamentoRequestDTO;
import com.clinica.agendamento.dto.AgendamentoResponseDTO;
import com.clinica.agendamento.exception.RecursoNaoEncontradoException;
import com.clinica.agendamento.exception.RegraNegocioException;
import com.clinica.agendamento.model.Agendamento;
import com.clinica.agendamento.model.Paciente;
import com.clinica.agendamento.model.Profissional;
import com.clinica.agendamento.model.StatusAgendamento;
import com.clinica.agendamento.repository.AgendamentoRepository;
import com.clinica.agendamento.repository.PacienteRepository;
import com.clinica.agendamento.repository.ProfissionalRepository;
import org.springframework.stereotype.Service;
 
import java.time.LocalDateTime;
import java.util.List;
// Service do agendamento, aqui definimos as regras de negocio do agendamento
// REGRA: nao permitir agendamento com data e hora no passado
// REGRA: nao permitir agendamento com paciente ou profissional inexistente
// REGRA: nao permitir agendamento com paciente ou profissional que ja tenha outro agendamento no mesmo horario

@Service
public class AgendamentoService {
 
    private final AgendamentoRepository agendamentoRepository;
    private final PacienteRepository pacienteRepository;
    private final ProfissionalRepository profissionalRepository;
 
    public AgendamentoService(AgendamentoRepository agendamentoRepository,
                              PacienteRepository pacienteRepository,
                              ProfissionalRepository profissionalRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.pacienteRepository = pacienteRepository;
        this.profissionalRepository = profissionalRepository;
    }
 
    //Cadastra um agendamento de acordo com as regras de negocio
    public AgendamentoResponseDTO criar(AgendamentoRequestDTO dto) {
 
        // Os IDs informados precisam existir. Se não, erro 404
        Paciente paciente = pacienteRepository.findById(dto.pacienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Paciente nao encontrado para o id " + dto.pacienteId()));
 
        Profissional profissional = profissionalRepository.findById(dto.profissionalId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Profissional nao encontrado para o id " + dto.profissionalId()));
 
        // não permitir agendamento no passado A anotacao @Future no DTO ja barra a maioria dos casos, mas ainda assim validei aqui
        //  tambem aplicamos uma tolerancia de 1 minuto: o campo datetime-local do front
        if (dto.dataHora().isBefore(LocalDateTime.now().minusMinutes(1))) {
            throw new RegraNegocioException("Nao e possivel agendar em data/hora no passado");
 
        // um profissional não pode ter dois agendamentos no mesmo horario, checamos se ja existe agendamento ATIVO 
        // StatusAgendamento diferente de CANCELADO para o mesmo profissional e horario
        boolean horarioOcupado = agendamentoRepository
                .existsByProfissionalIdAndDataHoraAndStatusNot(
                        profissional.getId(),
                        dto.dataHora(),
                        StatusAgendamento.CANCELADO);
 
        if (horarioOcupado) {
            throw new RegraNegocioException(
                    "O profissional ja possui um agendamento neste horario");
        }
 
        // com tudo validado, cria e salva, o construtor do Agendamento seta o status como AGENDADO por padrao
        Agendamento agendamento = new Agendamento(
                paciente, profissional, dto.dataHora(), dto.tipoAtendimento());
 
        Agendamento salvo = agendamentoRepository.save(agendamento);
        return AgendamentoResponseDTO.fromEntity(salvo);
    }
 
    // lista os agendamentos com filtros opcionais de paciente, profissional e status
     // qualquer combinação funciona, visto que parametros nulos são ignorados na query do repository
    public List<AgendamentoResponseDTO> listar(Long pacienteId, Long profissionalId,
                                               StatusAgendamento status) {
        return agendamentoRepository
                .buscarComFiltros(pacienteId, profissionalId, status)
                .stream()
                .map(AgendamentoResponseDTO::fromEntity)
                .toList();
    }
 
    // cancelamento de agendamento, altera o status para CANCELADO, registra o motivo e mantem o registro daquele agendamento
    // além disso impede que um agendamento que tenha o status CANCELADO seja cancelado novamente
    public AgendamentoResponseDTO cancelar(Long id, String motivo) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Agendamento nao encontrado para o id " + id));
 
        if (agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            throw new RegraNegocioException("Este agendamento ja esta cancelado");
        }
 
        agendamento.setStatus(StatusAgendamento.CANCELADO);
        agendamento.setMotivoCancelamento(motivo);
 
        // save() aqui ATUALIZA o registro existente (mesmo id), nao cria outro.
        Agendamento atualizado = agendamentoRepository.save(agendamento);
        return AgendamentoResponseDTO.fromEntity(atualizado);
    }
}
