import { Component, input, inject } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IDireccion } from '../direccion.model';
import { ITrabajador } from 'app/entities/trabajador/trabajador.model';
import { TrabajadorDetailComponent } from 'app/entities/trabajador/detail/trabajador-detail.component';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';
import { TrabajadorService } from 'app/entities/trabajador/service/trabajador.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteDetailComponent } from 'app/entities/paciente/detail/paciente-detail.component';

@Component({
  selector: 'jhi-direccion-detail',
  templateUrl: './direccion-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class DireccionDetailComponent {
  direccion = input<IDireccion | null>(null);

  protected readonly pacienteService = inject(PacienteService);
  protected readonly trabajadorService = inject(TrabajadorService);
  protected modalService = inject(NgbModal);

  previousState(): void {
    window.history.back();
  }
  //Metodo para abrir el modal detalle del trabajador
  openTrabajadorDetailModal(trabajador: ITrabajador): void {
    this.trabajadorService.find(trabajador.id).subscribe(response => {
      const modalRef = this.modalService.open(TrabajadorDetailComponent, { size: 'lg', backdrop: 'static' });
      modalRef.componentInstance.trabajador = response.body;
    });
  }
  //Metodo para abrir el modal detalle del paciente
  openPacienteDetailModal(paciente: IPaciente): void {
    this.pacienteService.find(paciente.id).subscribe(response => {
      const modalRef = this.modalService.open(PacienteDetailComponent, { size: 'lg', backdrop: 'static' });
      modalRef.componentInstance.paciente = response.body;
    });
  }
}
