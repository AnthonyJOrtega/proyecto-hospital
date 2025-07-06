import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITrabajador } from 'app/entities/trabajador/trabajador.model';
import { TrabajadorService } from 'app/entities/trabajador/service/trabajador.service';
import { IDireccion, NewDireccion } from 'app/entities/direccion/direccion.model';
import { DireccionService } from 'app/entities/direccion/service/direccion.service';
import { PacienteService } from '../service/paciente.service';
import { IPaciente } from '../paciente.model';
import { PacienteFormGroup, PacienteFormService } from './paciente-form.service';
import { CommonModule } from '@angular/common';
import { DireccionUpdateComponent } from 'app/entities/direccion/update/direccion-update.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-paciente-update',
  templateUrl: './paciente-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule, CommonModule, RouterModule],
})
export class PacienteUpdateComponent implements OnInit {
  isSaving = false;
  paciente: IPaciente | null = null;

  trabajadorsSharedCollection: ITrabajador[] = [];
  direccionsSharedCollection: IDireccion[] = [];

  protected pacienteService = inject(PacienteService);
  protected pacienteFormService = inject(PacienteFormService);
  protected trabajadorService = inject(TrabajadorService);
  protected direccionService = inject(DireccionService);
  protected activatedRoute = inject(ActivatedRoute);
  private modalService = inject(NgbModal);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PacienteFormGroup = this.pacienteFormService.createPacienteFormGroup();

  compareTrabajador = (o1: ITrabajador | null, o2: ITrabajador | null): boolean => this.trabajadorService.compareTrabajador(o1, o2);

  compareDireccion = (o1: IDireccion | null, o2: IDireccion | null): boolean => this.direccionService.compareDireccion(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paciente }) => {
      this.paciente = paciente;
      if (paciente) {
        this.updateForm(paciente);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const paciente = this.pacienteFormService.getPaciente(this.editForm);
    if (paciente.id !== null) {
      this.subscribeToSaveResponse(this.pacienteService.update(paciente));
    } else {
      this.subscribeToSaveResponse(this.pacienteService.create(paciente));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaciente>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(paciente: IPaciente): void {
    this.paciente = paciente;
    this.pacienteFormService.resetForm(this.editForm, paciente);

    this.trabajadorsSharedCollection = this.trabajadorService.addTrabajadorToCollectionIfMissing<ITrabajador>(
      this.trabajadorsSharedCollection,
      ...(paciente.trabajadors ?? []),
    );
    this.direccionsSharedCollection = this.direccionService.addDireccionToCollectionIfMissing<IDireccion>(
      this.direccionsSharedCollection,
      ...(paciente.direccions ?? []),
    );
    this.direccionSeleccionada = paciente.direccions && paciente.direccions.length > 0 ? paciente.direccions[0] : null;
  }

  protected loadRelationshipsOptions(): void {
    this.trabajadorService
      .query({ size: 9999 })
      .pipe(map((res: HttpResponse<ITrabajador[]>) => res.body ?? []))
      .pipe(
        map((trabajadors: ITrabajador[]) =>
          this.trabajadorService.addTrabajadorToCollectionIfMissing<ITrabajador>(trabajadors, ...(this.paciente?.trabajadors ?? [])),
        ),
      )
      .subscribe((trabajadors: ITrabajador[]) => (this.trabajadorsSharedCollection = trabajadors));

    this.direccionService
      .query({ size: 9999 })
      .pipe(map((res: HttpResponse<IDireccion[]>) => res.body ?? []))
      .pipe(
        map((direccions: IDireccion[]) =>
          this.direccionService.addDireccionToCollectionIfMissing<IDireccion>(direccions, ...(this.paciente?.direccions ?? [])),
        ),
      )
      .subscribe((direccions: IDireccion[]) => (this.direccionsSharedCollection = direccions));
  }
  // Metodos para manejar los trabajadores
  trabajadorInputText = '';

  addTrabajadorFromInput(): void {
    if ((this.editForm.value.trabajadors ?? []).length >= 1) {
      this.trabajadorInputText = '';
      return;
    }
    const input = this.trabajadorInputText?.trim().toLowerCase();
    if (!input) return;
    const trabajador = this.trabajadorsSharedCollection.find(t => (t.nombre + ' ' + t.apellido).toLowerCase() === input);
    if (trabajador) {
      this.editForm.patchValue({ trabajadors: [trabajador] });
      this.trabajadorInputText = '';
    }
  }

  removeTrabajador(trabajador: any): void {
    this.editForm.patchValue({ trabajadors: [] });
  }
  // Método para abrir el modal:
  abrirModalDireccion(): void {
    const modalRef = this.modalService.open(DireccionUpdateComponent, { size: 'lg', backdrop: 'static' });
    modalRef.closed.subscribe(() => {
      // Si quieres, recarga las direcciones aquí
      this.loadRelationshipsOptions();
    });
  }
  direccionInputText = '';
  direccionSeleccionada: IDireccion | null = null;

  addDireccionFromInput(): void {
    const input = this.direccionInputText?.trim().toLowerCase();
    if (!input) return;
    const direccion = this.direccionsSharedCollection.find(
      d =>
        (d.calle + ' --- C.P: ' + d.codigoPostal + ' --- nº:' + d.numero + ' --- (' + d.ciudad + ' ' + d.pais + ')').toLowerCase() ===
        input,
    );
    if (direccion) {
      this.direccionSeleccionada = direccion;
      this.editForm.patchValue({ direccions: [direccion] });
      this.direccionInputText = '';
    }
  }

  removeDireccion(): void {
    this.direccionSeleccionada = null;
    this.editForm.patchValue({ direccions: [] });
  }
}
