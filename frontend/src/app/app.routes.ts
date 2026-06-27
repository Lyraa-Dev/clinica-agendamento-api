import { Routes } from '@angular/router';
import { PacientesComponent } from './pages/pacientes/pacientes.component';
import { ProfissionaisComponent } from './pages/profissionais/profissionais.component';
import { AgendamentosComponent } from './pages/agendamentos/agendamentos.component';

export const routes: Routes = [
  { path: '', redirectTo: 'agendamentos', pathMatch: 'full' },
  { path: 'pacientes', component: PacientesComponent },
  { path: 'profissionais', component: ProfissionaisComponent },
  { path: 'agendamentos', component: AgendamentosComponent },
  { path: '**', redirectTo: 'agendamentos' }
];