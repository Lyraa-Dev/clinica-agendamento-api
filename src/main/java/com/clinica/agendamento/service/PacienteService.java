package com.clinica.agendamento.service;
 
import com.clinica.agendamento.dto.PacienteRequestDTO;
import com.clinica.agendamento.dto.PacienteResponseDTO;
import com.clinica.agendamento.exception.RegraNegocioException;
import com.clinica.agendamento.model.Paciente;
import com.clinica.agendamento.repository.PacienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
import java.util.List;

// Service do paciente, aqui definimos as regras de negocio do paciente
// aqui ele recebe a requisição do controller e chama este service

@Service
public class PacienteService {
 
    private final PacienteRepository pacienteRepository;

    // Construtor do service, injetando o repository do paciente
    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    // metodo para cadastrar um paciente, REGRA: não pode cadastrar paciente com cpf duplicado
    @Transactional
    public PacienteResponseDTO cadastrar(PacienteRequestDTO dto) {
        if (pacienteRepository.existsByCpf(dto.cpf())) {
            throw new RegraNegocioException("Ja existe um paciente cadastrado com o CPF informado");
        }
 
        Paciente paciente = new Paciente(dto.nome(), dto.cpf(), dto.telefone());
        Paciente salvo = pacienteRepository.save(paciente);
 
        return PacienteResponseDTO.fromEntity(salvo);
    }

    // metodo para listar todos os pacientes
    @Transactional(readOnly = true)
    public List<PacienteResponseDTO> listar() {
        return pacienteRepository.findAll()
                .stream()
                .map(PacienteResponseDTO::fromEntity)
                .toList();
    }
}
