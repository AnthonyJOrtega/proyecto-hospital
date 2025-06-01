import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { ICita } from '../cita.model';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteDetailComponent } from 'app/entities/paciente/detail/paciente-detail.component';
import { ITrabajador } from 'app/entities/trabajador/trabajador.model';
import { TrabajadorDetailComponent } from 'app/entities/trabajador/detail/trabajador-detail.component';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';
import { TrabajadorService } from 'app/entities/trabajador/service/trabajador.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-cita-detail',
  templateUrl: './cita-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class CitaDetailComponent {
  cita = input<ICita | null>(null);
  constructor(
    protected pacienteService: PacienteService,
    protected modalService: NgbModal,
    protected trabajadorService: TrabajadorService,
  ) {}

  previousState(): void {
    window.history.back();
  }
  //Metodo para abrir el modal de detalle del paciente
  openPacienteDetailModal(paciente: IPaciente): void {
    this.pacienteService.find(paciente.id).subscribe(response => {
      const modalRef = this.modalService.open(PacienteDetailComponent, { size: 'lg', backdrop: 'static' });
      modalRef.componentInstance.paciente = response.body;
    });
  }
  //Metodo para abrir el modal detalle del trabajador
  openTrabajadorDetailModal(trabajador: ITrabajador): void {
    this.trabajadorService.find(trabajador.id).subscribe(response => {
      const modalRef = this.modalService.open(TrabajadorDetailComponent, { size: 'lg', backdrop: 'static' });
      modalRef.componentInstance.trabajador = response.body;
    });
  }
}
