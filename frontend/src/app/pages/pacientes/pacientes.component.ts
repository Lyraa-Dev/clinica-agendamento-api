import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { Paciente } from '../../models/models';

@Component({
  selector: 'app-pacientes',
  imports: [CommonModule, FormsModule],
  templateUrl: './pacientes.component.html'
})
export class PacientesComponent implements OnInit {

  pacientes = signal<Paciente[]>([]);
  novo: Paciente = { nome: '', cpf: '', telefone: '' };
  busca = '';
  erro = signal<string>('');
  sucesso = signal<string>('');

  constructor(private api: ApiService) {}

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.api.listarPacientes().subscribe({
      next: (lista) => this.pacientes.set(lista),
      error: () => this.erro.set('Nao foi possivel carregar os pacientes. A API esta no ar?')
    });
  }

  // Filtra a lista por nome, ID ou CPF conforme o usuario digita.
  pacientesFiltrados(): Paciente[] {
    const termo = this.busca.trim().toLowerCase();
    if (!termo) return this.pacientes();
    return this.pacientes().filter(p =>
      p.nome.toLowerCase().includes(termo) ||
      p.cpf.includes(termo) ||
      String(p.id).includes(termo)
    );
  }

  cadastrar(): void {
    this.erro.set('');
    this.sucesso.set('');
    this.api.cadastrarPaciente(this.novo).subscribe({
      next: () => {
        this.sucesso.set('Paciente cadastrado com sucesso.');
        this.novo = { nome: '', cpf: '', telefone: '' };
        this.carregar();
      },
      error: (e) => this.erro.set(e?.error?.mensagem || 'Erro ao cadastrar paciente.')
    });
  }
}