package com.clinica.agendamento.exception;
 
import com.clinica.agendamento.dto.ErroResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
 
import java.time.LocalDateTime;
import java.util.stream.Collectors;

// tratamento generalizado de erros da aplicação
// @RestControllerAdvice intercepta exceções lancadas por qualquer controller
// e as traduz em respostas HTTP padronizadas. Assim, os controllers e services
// ficam limpos (so lancam a exceção) e a resposta de erro e consistente em toda a API.

@RestControllerAdvice
public class GlobalExceptionHandler {
    // trata violações de regra de negocio, retornando 400 Bad Request
    // ex: horario ocupado, data no passado, CPF/Registro duplicado, etc
    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResponseDTO> tratarRegraNegocio(RegraNegocioException ex) {
        ErroResponseDTO erro = new ErroResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Regra de negocio violada",
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }
    // trata recursos nao encontrados, retornando 404 Not Found
    // ex: id de paciente, profissional ou agendamento inexistente

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponseDTO> tratarNaoEncontrado(RecursoNaoEncontradoException ex) {
        ErroResponseDTO erro = new ErroResponseDTO(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Recurso nao encontrado",
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }
    // falhas de validação nos DTOs, retornando 400 Bad Request ex: (@NotBlank, @Future, etc)
    // agrupa todas as mensagens numa só para o cliente saber o que é necessario corrigir
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponseDTO> tratarValidacao(MethodArgumentNotValidException ex) {
        String mensagens = ex.getBindingResult().getFieldErrors().stream()
                .map(campo -> campo.getField() + ": " + campo.getDefaultMessage())
                .collect(Collectors.joining("; "));
 
        ErroResponseDTO erro = new ErroResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validacao",
                mensagens);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }
    // trata qualquer outra exceção não prevista, retornando 500 Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponseDTO> tratarErroGenerico(Exception ex) {
        ErroResponseDTO erro = new ErroResponseDTO(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno",
                "Ocorreu um erro inesperado. Tente novamente.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }
}