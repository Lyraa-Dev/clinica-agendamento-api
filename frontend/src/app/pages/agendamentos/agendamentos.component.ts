import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import {
  Paciente, Profissional, Agendamento, AgendamentoRequest, StatusAgendamento
} from '../../models/models';

@Component({
  selector: 'app-agendamentos',
  imports: [CommonModule, FormsModule],
  templateUrl: './agendamentos.component.html'
})
export class AgendamentosComponent implements OnInit {

  agendamentos = signal<Agendamento[]>([]);
  pacientes = signal<Paciente[]>([]);
  profissionais = signal<Profissional[]>([]);

  // ---- Novo agendamento ----
  // Busca de paciente: o usuario digita nome, ID ou CPF.
  buscaPaciente = '';
  pacienteSelecionado: Paciente | null = null;

  // Fluxo especialidade -> profissional.
  especialidadeSelecionada = '';
  profissionalSelecionadoId = 0;

  dataHora = '';
  tipoAtendimento = '';

  // filtros da listagem
  filtroPaciente: number | null = null;
  filtroProfissional: number | null = null;
  filtroStatus: StatusAgendamento | null = null;

  erro = signal<string>('');
  sucesso = signal<string>('');

  constructor(private api: ApiService) {}

  ngOnInit(): void {
    this.carregarApoio();
    this.carregar();
  }

  carregarApoio(): void {
    this.api.listarPacientes().subscribe({ next: (l) => this.pacientes.set(l) });
    this.api.listarProfissionais().subscribe({ next: (l) => this.profissionais.set(l) });
  }

  // Lista de especialidades unicas, derivada dos profissionais cadastrados.
  especialidades = computed(() => {
    const set = new Set(this.profissionais().map(p => p.especialidade));
    return Array.from(set).sort();
  });

  // Profissionais filtrados pela especialidade escolhida.
  profissionaisDaEspecialidade(): Profissional[] {
    if (!this.especialidadeSelecionada) return [];
    return this.profissionais()
      .filter(p => p.especialidade === this.especialidadeSelecionada);
  }

  // Pacientes que batem com a busca (nome, ID ou CPF). Mostra ate 8 sugestoes.
  pacientesFiltrados(): Paciente[] {
    const termo = this.buscaPaciente.trim().toLowerCase();
    if (!termo) return [];
    return this.pacientes().filter(p =>
      p.nome.toLowerCase().includes(termo) ||
      p.cpf.includes(termo) ||
      String(p.id).includes(termo)
    ).slice(0, 8);
  }

  selecionarPaciente(p: Paciente): void {
    this.pacienteSelecionado = p;
    this.buscaPaciente = `C${p.id} — ${p.nome}`;
  }

  // Ao trocar de especialidade, zera o profissional escolhido.
  onEspecialidadeChange(): void {
    this.profissionalSelecionadoId = 0;
  }

  criar(): void {
    this.erro.set('');
    this.sucesso.set('');

    if (!this.pacienteSelecionado) {
      this.erro.set('Selecione um paciente na busca.');
      return;
    }
    if (!this.profissionalSelecionadoId) {
      this.erro.set('Selecione a especialidade e o profissional.');
      return;
    }

    let dh = this.dataHora;
    if (dh && dh.length === 16) dh = dh + ':00'; // datetime-local sem segundos

    const req: AgendamentoRequest = {
      pacienteId: this.pacienteSelecionado.id!,
      profissionalId: this.profissionalSelecionadoId,
      dataHora: dh,
      tipoAtendimento: this.tipoAtendimento
    };

    this.api.criarAgendamento(req).subscribe({
      next: () => {
        this.sucesso.set('Agendamento criado com sucesso.');
        this.limparFormulario();
        this.carregar();
      },
      error: (e) => this.erro.set(e?.error?.mensagem || 'Erro ao criar agendamento.')
    });
  }

  limparFormulario(): void {
    this.buscaPaciente = '';
    this.pacienteSelecionado = null;
    this.especialidadeSelecionada = '';
    this.profissionalSelecionadoId = 0;
    this.dataHora = '';
    this.tipoAtendimento = '';
  }

  carregar(): void {
    this.api.listarAgendamentos({
      pacienteId: this.filtroPaciente || undefined,
      profissionalId: this.filtroProfissional || undefined,
      status: this.filtroStatus || undefined
    }).subscribe({
      next: (l) => this.agendamentos.set(l),
      error: () => this.erro.set('Nao foi possivel carregar os agendamentos. A API esta no ar?')
    });
  }

  cancelar(ag: Agendamento): void {
    const motivo = prompt('Informe o motivo do cancelamento:');
    if (!motivo) return;
    this.api.cancelarAgendamento(ag.id, motivo).subscribe({
      next: () => { this.sucesso.set('Agendamento cancelado.'); this.carregar(); },
      error: (e) => this.erro.set(e?.error?.mensagem || 'Erro ao cancelar.')
    });
  }

  limparFiltros(): void {
    this.filtroPaciente = null;
    this.filtroProfissional = null;
    this.filtroStatus = null;
    this.carregar();
  }
}