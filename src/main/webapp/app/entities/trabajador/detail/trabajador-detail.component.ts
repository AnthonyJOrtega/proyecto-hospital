import { Component, Input } from '@angular/core';
import { NavigationStart, RouterModule } from '@angular/router';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITrabajador } from '../trabajador.model';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteDetailComponent } from 'app/entities/paciente/detail/paciente-detail.component';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-trabajador-detail',
  templateUrl: './trabajador-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class TrabajadorDetailComponent {
  @Input() trabajador: ITrabajador | null = null;
  navigationSubscription: any;

  // Constructor para TrabajadorDetailComponent
  // Inyectamos NgbActiveModal para manejar el modal
  // Inyectamos NgbModal para abrir otros modales
  // Inyectamos PacienteService para manejar pacientes
  // Inyectamos Router para manejar la navegación
  // navigationSubscription se usa para cerrar el modal al navegar a otra ruta
  // Esto nos permitira cerrar el modal cuando el usuario navegue a otra ruta, evitando que se quede abierto en la nueva vista.
  constructor(
    public activeModal: NgbActiveModal,
    private modalService: NgbModal,
    private pacienteService: PacienteService,
    private router: Router,
  ) {
    // Cierra todos los modales al navegar
    this.navigationSubscription = this.router.events.subscribe(event => {
      if (event instanceof NavigationStart) {
        this.activeModal.dismiss();
      }
    });
  }

  close(): void {
    this.activeModal.dismiss();
  }
  openPacienteDetailModal(paciente: IPaciente): void {
    this.pacienteService.find(paciente.id).subscribe(response => {
      const modalRef = this.modalService.open(PacienteDetailComponent, { size: 'lg', backdrop: 'static' });
      modalRef.componentInstance.paciente = response.body;
    });
  }
}
