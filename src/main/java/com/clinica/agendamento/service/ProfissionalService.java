package com.clinica.agendamento.service;

import com.clinica.agendamento.dto.ProfissionalRequestDTO;
import com.clinica.agendamento.dto.ProfissionalResponseDTO;
import com.clinica.agendamento.exception.RegraNegocioException;
import com.clinica.agendamento.model.Profissional;
import com.clinica.agendamento.repository.ProfissionalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

 
import java.util.List;

// Service do profissional, aqui definimos as regras de negocio do profissional
@Service
public class ProfissionalService {
 
    private final ProfissionalRepository profissionalRepository;
 
    public ProfissionalService(ProfissionalRepository profissionalRepository) {
        this.profissionalRepository = profissionalRepository;
    }
 
     // Cadastra um profissional.
     // REGRA: nao permitir dois profissionais com o mesmo registro (CRM/CRO).
     
    @Transactional
    public ProfissionalResponseDTO cadastrar(ProfissionalRequestDTO dto) {
        if (profissionalRepository.existsByRegistro(dto.registro())) {
            throw new RegraNegocioException("Ja existe um profissional cadastrado com o registro informado");
        }
 
        Profissional profissional = new Profissional(dto.nome(), dto.especialidade(), dto.registro());
        Profissional salvo = profissionalRepository.save(profissional);
        return ProfissionalResponseDTO.fromEntity(salvo);
    }
 
    @Transactional(readOnly = true)
    public List<ProfissionalResponseDTO> listar() {
        return profissionalRepository.findAll()
                .stream()
                .map(ProfissionalResponseDTO::fromEntity)
                .toList();
    }
}