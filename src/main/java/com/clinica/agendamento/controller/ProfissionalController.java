package com.clinica.agendamento.controller;

import com.clinica.agendamento.dto.ProfissionalRequestDTO;
import com.clinica.agendamento.dto.ProfissionalResponseDTO;
import com.clinica.agendamento.service.ProfissionalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;

// Controller REST do profissional, aqui definimos os endpoints
// como estou usando o H2 que é um banco de dados em memoria, decidi implementar o endpoint CRUD para o profissional

@RestController
@RequestMapping("/profissionais")
public class ProfissionalController {
 
    private final ProfissionalService profissionalService;
 
    public ProfissionalController(ProfissionalService profissionalService) {
        this.profissionalService = profissionalService;
    }
 
    @PostMapping
    public ResponseEntity<ProfissionalResponseDTO> cadastrar(@Valid @RequestBody ProfissionalRequestDTO dto) {
        ProfissionalResponseDTO criado = profissionalService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }
 
    @GetMapping
    public ResponseEntity<List<ProfissionalResponseDTO>> listar() {
        return ResponseEntity.ok(profissionalService.listar());
    }
}