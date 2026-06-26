package com.clinica.agendamento.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Entidade Agendamento, representa o agendamento de um atendimento, ligando um paciente a um profissional em uma determinada data e hora

@Entity
@Table(name = "agendamento")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

// como alguns agendamentos podem pertencer ao mesmo paciente(com diferentes profissionais e/ou horarios) utilizei a relação N:1 
// (A mesma regra se aplica para o profissional, que pode ter vários agendamentos com diferentes pacientes e horários)
// fetch LAZY para evitar que o paciente seja carregado junto com o agendamento, sendo carregado apenas quando acessado
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "profissional_id", nullable = false)
    private Profissional profissional;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;
 
    @Column(name = "tipo_atendimento", nullable = false, length = 80)

    private String tipoAtendimento;

    // Enum para representar os status de um agendamento, garantindo que apenas os valores definidos no enum possam ser utilizados 
    // como status de agendamento

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusAgendamento status;
 
    // motivo do cancelamento deve ser preenchido apenas quando o agendamento for cancelado.
    @Column(name = "motivo_cancelamento", length = 255)
    private String motivoCancelamento;
 
    public Agendamento() {
    }
 
    public Agendamento(Paciente paciente, Profissional profissional,
                       LocalDateTime dataHora, String tipoAtendimento) {
        this.paciente = paciente;
        this.profissional = profissional;
        this.dataHora = dataHora;
        this.tipoAtendimento = tipoAtendimento;
        this.status = StatusAgendamento.AGENDADO; // todo agendamento inicia com o Status de AGENDADO
    }
 
    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public Paciente getPaciente() {
        return paciente;
    }
 
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
 
    public Profissional getProfissional() {
        return profissional;
    }
 
    public void setProfissional(Profissional profissional) {
        this.profissional = profissional;
    }
 
    public LocalDateTime getDataHora() {
        return dataHora;
    }
 
    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
 
    public String getTipoAtendimento() {
        return tipoAtendimento;
    }
 
    public void setTipoAtendimento(String tipoAtendimento) {
        this.tipoAtendimento = tipoAtendimento;
    }
 
    public StatusAgendamento getStatus() {
        return status;
    }
 
    public void setStatus(StatusAgendamento status) {
        this.status = status;
    }
 
    public String getMotivoCancelamento() {
        return motivoCancelamento;
    }
 
    public void setMotivoCancelamento(String motivoCancelamento) {
        this.motivoCancelamento = motivoCancelamento;
    }
}
