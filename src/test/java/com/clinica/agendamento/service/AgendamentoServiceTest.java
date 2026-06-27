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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
 
import java.time.LocalDateTime;
import java.util.Optional;
 
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

// Testes unitarios do service de agendamento, aqui testamos as regras de negocio do agendamento
// o foco é testar as regras de negocio de forma isolada, os repositorios são mockados, ou seja, não acessam o banco de dados, apenas
// simulam o comportamento dos repositorios, retornando valores predefinidos para os testes

@ExtendWith(MockitoExtension.class)
class AgendamentoServiceTest {
 
    // @Mock cria um duble de cada repository.
    @Mock
    private AgendamentoRepository agendamentoRepository;
    @Mock
    private PacienteRepository pacienteRepository;
    @Mock
    private ProfissionalRepository profissionalRepository;
 
    // @InjectMocks cria o service real e injeta os mocks acima nele.
    @InjectMocks
    private AgendamentoService agendamentoService;
 
    // ---- helpers para montar dados de teste ----
 
    private Paciente pacienteFake() {
        Paciente p = new Paciente("Maria Silva", "12345678901", "81999998888");
        p.setId(1L);
        return p;
    }
 
    private Profissional profissionalFake() {
        Profissional prof = new Profissional("Dr. Carlos", "Cardiologia", "CRM-PE 12345");
        prof.setId(1L);
        return prof;
    }
 
    @Test
    @DisplayName("Deve criar agendamento quando todos os dados sao validos")
    void deveCriarAgendamentoValido() {
        // Arrange: data no futuro e mocks configurados para facilitar o teste
        LocalDateTime futuro = LocalDateTime.now().plusDays(5);
        AgendamentoRequestDTO dto = new AgendamentoRequestDTO(1L, 1L, futuro, "Consulta");
 
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(pacienteFake()));
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissionalFake()));
        when(agendamentoRepository.existsByProfissionalIdAndDataHoraAndStatusNot(
                anyLong(), any(), any())).thenReturn(false); // horario livre
        // quando salvar, devolve o proprio objeto recebido
        when(agendamentoRepository.save(any(Agendamento.class)))
                .thenAnswer(invocacao -> invocacao.getArgument(0));
 
        // Act
        AgendamentoResponseDTO resultado = agendamentoService.criar(dto);
 
        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.status()).isEqualTo(StatusAgendamento.AGENDADO);
        assertThat(resultado.pacienteNome()).isEqualTo("Maria Silva");
        verify(agendamentoRepository).save(any(Agendamento.class)); // salvou de fato
    }
 
    @Test
    @DisplayName("Nao deve criar agendamento quando o profissional ja tem o horario ocupado")
    void naoDeveCriarQuandoHorarioOcupado() {
        // Arrange: o mock de checagem de horario retorna TRUE (ocupado)
        LocalDateTime futuro = LocalDateTime.now().plusDays(5);
        AgendamentoRequestDTO dto = new AgendamentoRequestDTO(1L, 1L, futuro, "Consulta");
 
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(pacienteFake()));
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissionalFake()));
        when(agendamentoRepository.existsByProfissionalIdAndDataHoraAndStatusNot(
                anyLong(), any(), any())).thenReturn(true); // horario OCUPADO
 
        // Act + Assert: espera a excecao de regra de negocio
        assertThatThrownBy(() -> agendamentoService.criar(dto))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessageContaining("ja possui um agendamento neste horario");
 
        // E garante que NUNCA salvou (a regra barrou antes)
        verify(agendamentoRepository, never()).save(any());
    }
 
    @Test
    @DisplayName("Nao deve criar agendamento com data/hora no passado")
    void naoDeveCriarComDataNoPassado() {
        // Arrange: data no passado
        LocalDateTime passado = LocalDateTime.now().minusDays(1);
        AgendamentoRequestDTO dto = new AgendamentoRequestDTO(1L, 1L, passado, "Consulta");
 
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(pacienteFake()));
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissionalFake()));
 
        // Act + Assert
        assertThatThrownBy(() -> agendamentoService.criar(dto))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessageContaining("passado");
 
        verify(agendamentoRepository, never()).save(any());
    }
 
    @Test
    @DisplayName("Deve lancar 'nao encontrado' quando o paciente nao existe")
    void naoDeveCriarQuandoPacienteInexistente() {
        // Arrange: o paciente nao existe (Optional vazio)
        LocalDateTime futuro = LocalDateTime.now().plusDays(5);
        AgendamentoRequestDTO dto = new AgendamentoRequestDTO(99L, 1L, futuro, "Consulta");
 
        when(pacienteRepository.findById(99L)).thenReturn(Optional.empty());
 
        // Act + Assert
        assertThatThrownBy(() -> agendamentoService.criar(dto))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("Paciente nao encontrado");
    }
 
    @Test
    @DisplayName("Deve cancelar agendamento mudando status para CANCELADO e mantendo o registro")
    void deveCancelarAgendamento() {
        // Arrange: um agendamento existente, no estado AGENDADO
        Agendamento existente = new Agendamento(
                pacienteFake(), profissionalFake(),
                LocalDateTime.now().plusDays(5), "Consulta");
        existente.setId(10L);
 
        when(agendamentoRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(agendamentoRepository.save(any(Agendamento.class)))
                .thenAnswer(invocacao -> invocacao.getArgument(0));
 
        // Act
        AgendamentoResponseDTO resultado =
                agendamentoService.cancelar(10L, "Paciente solicitou remarcacao");
 
        // Assert: status mudou, motivo gravado, e o save foi chamado (registro mantido/atualizado)
        assertThat(resultado.status()).isEqualTo(StatusAgendamento.CANCELADO);
        assertThat(resultado.motivoCancelamento()).isEqualTo("Paciente solicitou remarcacao");
        verify(agendamentoRepository).save(existente);
    }
}