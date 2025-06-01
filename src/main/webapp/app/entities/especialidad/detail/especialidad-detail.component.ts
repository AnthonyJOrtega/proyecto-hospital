import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IEspecialidad } from '../especialidad.model';
import { ITrabajador } from 'app/entities/trabajador/trabajador.model';
import { TrabajadorDetailComponent } from 'app/entities/trabajador/detail/trabajador-detail.component';
import { TrabajadorService } from 'app/entities/trabajador/service/trabajador.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-especialidad-detail',
  templateUrl: './especialidad-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class EspecialidadDetailComponent {
  especialidad = input<IEspecialidad | null>(null);

  constructor(
    private trabajadorService: TrabajadorService,
    private modalService: NgbModal,
  ) {}

  previousState(): void {
    window.history.back();
  }
  openTrabajadorDetailModal(trabajador: ITrabajador): void {
    this.trabajadorService.find(trabajador.id).subscribe(response => {
      const modalRef = this.modalService.open(TrabajadorDetailComponent, { size: 'lg', backdrop: 'static' });
      modalRef.componentInstance.trabajador = response.body;
    });
  }
}
