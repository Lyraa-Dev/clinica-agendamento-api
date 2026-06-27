package com.clinica.agendamento.controller;
 
import com.clinica.agendamento.dto.AgendamentoRequestDTO;
import com.clinica.agendamento.dto.AgendamentoResponseDTO;
import com.clinica.agendamento.dto.CancelamentoRequestDTO;
import com.clinica.agendamento.model.StatusAgendamento;
import com.clinica.agendamento.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;

// Controller do agendamento, aqui definimos os endpoints da API para o agendamento (Cria, listar (com filtros) e cancelar agendamento)
@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {
 
    private final AgendamentoService agendamentoService;
 
    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }
    // POST /agendamentos - cria um agendamento
    @PostMapping
    public ResponseEntity<AgendamentoResponseDTO> criar(@Valid @RequestBody AgendamentoRequestDTO dto) {
        AgendamentoResponseDTO criado = agendamentoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }
    // GET /agendamentos - lista agendamentos
    // Aceita filtros OPCIONAIS via query params: /agendamentos?pacienteId=1  | /agendamentos?profissionalId=2&status=AGENDADO
    // caso nenhum parametro seja informado, retorna todos os agendamentos
    @GetMapping
    public ResponseEntity<List<AgendamentoResponseDTO>> listar(
            @RequestParam(required = false) Long pacienteId,
            @RequestParam(required = false) Long profissionalId,
            @RequestParam(required = false) StatusAgendamento status) {
        return ResponseEntity.ok(
                agendamentoService.listar(pacienteId, profissionalId, status));
    }

    // PATCH /agendamentos/{id}/cancelamento. Cancela um agendamento
    // decidi usar PATCH ao inves de DELETE, pois o agendamento nao e removido do banco, apenas seu status e alterado para CANCELADO
    // e é adicionado o motivo do cancelamento
    @PatchMapping("/{id}/cancelamento")
    public ResponseEntity<AgendamentoResponseDTO> cancelar(
            @PathVariable Long id,
            @Valid @RequestBody CancelamentoRequestDTO dto) {
        return ResponseEntity.ok(agendamentoService.cancelar(id, dto.motivo()));
    }
}