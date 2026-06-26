package com.clinica.agendamento.controller;

import com.clinica.agendamento.dto.PacienteRequestDTO;
import com.clinica.agendamento.dto.PacienteResponseDTO;
import com.clinica.agendamento.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
// Controller REST do paciente, aqui definimos os endpoints
// aqui ele recebe a requisição, repassa ao service e devolve a resposta HTTP adequada
// aqui não tem regra de negocio, apenas validação de dados e repasse para o service

@RestController
@RequestMapping("/pacientes")

public class PacienteController {
    
    private final PacienteService pacienteService;
 
    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }
    // post /pacientes - cria um novo paciente
    // @Valid dispara as validações do DTO e retorna a resposta HTTP
    @PostMapping
    public ResponseEntity<PacienteResponseDTO> cadastrar(@Valid @RequestBody PacienteRequestDTO dto) {
        PacienteResponseDTO criado = pacienteService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }
    // get /pacientes - lista todos os pacientes
    @GetMapping
    public ResponseEntity<List<PacienteResponseDTO>> listar() {
        return ResponseEntity.ok(pacienteService.listar());
    }
}
