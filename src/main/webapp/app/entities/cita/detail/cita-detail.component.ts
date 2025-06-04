import { Component, Input, input, Optional, OnInit } from '@angular/core';
import { NavigationStart, Router, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { ICita } from '../cita.model';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteDetailComponent } from 'app/entities/paciente/detail/paciente-detail.component';
import { ITrabajador } from 'app/entities/trabajador/trabajador.model';
import { TrabajadorDetailComponent } from 'app/entities/trabajador/detail/trabajador-detail.component';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';
import { TrabajadorService } from 'app/entities/trabajador/service/trabajador.service';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CitaService } from '../service/cita.service';
import { HttpResponse } from '@angular/common/http';
import { filter, map, tap } from 'rxjs/operators';
import { IInforme } from 'app/entities/informe/informe.model';
import { InformeService } from 'app/entities/informe/service/informe.service';
import { CitaDeleteDialogComponent } from '../delete/cita-delete-dialog.component';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  selector: 'jhi-cita-detail',
  templateUrl: './cita-detail.component.html',
  standalone: true,
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class CitaDetailComponent {
  @Input() citaId?: number;
  cita = input<ICita | null>(null);
  isModal = false;

  constructor(
    protected pacienteService: PacienteService,
    protected modalService: NgbModal,
    protected citaService: CitaService,
    protected trabajadorService: TrabajadorService,
    protected router: Router,
    @Optional() public activeModal: NgbActiveModal,
  ) {
    this.isModal = !!activeModal;
  }
  citaLocal: ICita | null = null;
  private routerSubscription: any;

  ngOnInit(): void {
    // para hacer que el modal se cierre al navegar a otra ruta
    if (this.isModal) {
      this.routerSubscription = this.router.events.subscribe(event => {
        if (event instanceof NavigationStart) {
          this.activeModal.dismiss();
        }
      });
    }
    // Si se pasa un ID de cita, se busca la cita correspondiente
    if (this.citaId) {
      this.citaService.find(this.citaId).subscribe(res => {
        this.citaLocal = res.body;
      });
    }
  }

  ngOnDestroy(): void {
    if (this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
  }
  previousState(): void {
    if (this.isModal) {
      this.activeModal.dismiss();
    } else {
      window.history.back();
    }
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
  //Metodo para eliminar la cita
}
