import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { IPaciente } from '../paciente.model';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ITrabajador } from 'app/entities/trabajador/trabajador.model';
import { TrabajadorService } from 'app/entities/trabajador/service/trabajador.service';
import { TrabajadorDetailComponent } from 'app/entities/trabajador/detail/trabajador-detail.component';

@Component({
  selector: 'jhi-paciente-detail',
  templateUrl: './paciente-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class PacienteDetailComponent {
  @Input() paciente: IPaciente | null = null;
  constructor(
    public activeModal: NgbActiveModal,
    private trabajadorService: TrabajadorService,
    public modalService: NgbModal,
  ) {}

  close(): void {
    this.activeModal.dismiss(); // Cierra el modal
  }

  openTrabajadorDetailModal(trabajador: ITrabajador): void {
    this.trabajadorService.find(trabajador.id).subscribe(response => {
      const modalRef = this.modalService.open(TrabajadorDetailComponent, { size: 'lg', backdrop: 'static' });
      modalRef.componentInstance.trabajador = response.body;
    });
  }
}
