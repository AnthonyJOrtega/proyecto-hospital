import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';
import { ITrabajador } from 'app/entities/trabajador/trabajador.model';
import { TrabajadorService } from 'app/entities/trabajador/service/trabajador.service';
import { DireccionService } from '../service/direccion.service';
import { IDireccion } from '../direccion.model';
import { DireccionFormGroup, DireccionFormService } from './direccion-form.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-direccion-update',
  templateUrl: './direccion-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DireccionUpdateComponent implements OnInit {
  isSaving = false;
  direccion: IDireccion | null = null;

  pacientesSharedCollection: IPaciente[] = [];
  trabajadorsSharedCollection: ITrabajador[] = [];

  protected direccionService = inject(DireccionService);
  protected direccionFormService = inject(DireccionFormService);
  protected pacienteService = inject(PacienteService);
  protected trabajadorService = inject(TrabajadorService);
  protected activatedRoute = inject(ActivatedRoute);
  public activeModal: NgbActiveModal | null = null;
  constructor() {
    try {
      this.activeModal = inject(NgbActiveModal);
    } catch (e) {
      // No está en un modal, no pasa nada
    }
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DireccionFormGroup = this.direccionFormService.createDireccionFormGroup();

  comparePaciente = (o1: IPaciente | null, o2: IPaciente | null): boolean => this.pacienteService.comparePaciente(o1, o2);

  compareTrabajador = (o1: ITrabajador | null, o2: ITrabajador | null): boolean => this.trabajadorService.compareTrabajador(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ direccion }) => {
      this.direccion = direccion;
      if (direccion) {
        this.updateForm(direccion);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const direccion = this.direccionFormService.getDireccion(this.editForm);
    if (direccion.id !== null) {
      this.subscribeToSaveResponse(this.direccionService.update(direccion));
    } else {
      this.subscribeToSaveResponse(this.direccionService.create(direccion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDireccion>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: res => this.onSaveSuccess(res.body ?? undefined),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(direccion?: IDireccion): void {
    if (this.activeModal && typeof this.activeModal.close === 'function') {
      this.activeModal.close(direccion ?? this.direccion ?? undefined);
    } else {
      // Redirige a la lista de direcciones tras guardar
      window.location.href = '/direccion';
    }
  }

  protected onSaveError(): void {
    // Api for inheritance.
    if (this.activeModal) {
      this.activeModal.dismiss('error');
    }
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(direccion: IDireccion): void {
    this.direccion = direccion;
    this.direccionFormService.resetForm(this.editForm, direccion);

    this.pacientesSharedCollection = this.pacienteService.addPacienteToCollectionIfMissing<IPaciente>(
      this.pacientesSharedCollection,
      ...(direccion.pacientes ?? []),
    );
    this.trabajadorsSharedCollection = this.trabajadorService.addTrabajadorToCollectionIfMissing<ITrabajador>(
      this.trabajadorsSharedCollection,
      ...(direccion.trabajadors ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.pacienteService
      .query()
      .pipe(map((res: HttpResponse<IPaciente[]>) => res.body ?? []))
      .pipe(
        map((pacientes: IPaciente[]) =>
          this.pacienteService.addPacienteToCollectionIfMissing<IPaciente>(pacientes, ...(this.direccion?.pacientes ?? [])),
        ),
      )
      .subscribe((pacientes: IPaciente[]) => (this.pacientesSharedCollection = pacientes));

    this.trabajadorService
      .query()
      .pipe(map((res: HttpResponse<ITrabajador[]>) => res.body ?? []))
      .pipe(
        map((trabajadors: ITrabajador[]) =>
          this.trabajadorService.addTrabajadorToCollectionIfMissing<ITrabajador>(trabajadors, ...(this.direccion?.trabajadors ?? [])),
        ),
      )
      .subscribe((trabajadors: ITrabajador[]) => (this.trabajadorsSharedCollection = trabajadors));
  }
  trabajadorInputText = '';

  addTrabajadorFromInput(): void {
    const input = this.trabajadorInputText?.trim().toLowerCase();
    if (!input) return;
    const trabajador = this.trabajadorsSharedCollection.find(t => (t.nombre + ' ' + t.apellido).toLowerCase() === input);
    if (trabajador) {
      const current = this.editForm.value.trabajadors ?? [];
      if (!current.some((t: any) => t.id === trabajador.id)) {
        this.editForm.patchValue({ trabajadors: [...current, trabajador] });
      }
      this.trabajadorInputText = '';
    }
  }

  removeTrabajador(trabajador: any): void {
    const current = this.editForm.value.trabajadors ?? [];
    this.editForm.patchValue({ trabajadors: current.filter((t: any) => t.id !== trabajador.id) });
  }

  pacienteInputText = '';

  addPacienteFromInput(): void {
    const input = this.pacienteInputText?.trim().toLowerCase();
    if (!input) return;
    const paciente = this.pacientesSharedCollection.find(p => (p.nombre + ' ' + p.apellido).toLowerCase() === input);
    if (paciente) {
      const current = this.editForm.value.pacientes ?? [];
      if (!current.some((p: any) => p.id === paciente.id)) {
        this.editForm.patchValue({ pacientes: [...current, paciente] });
      }
      this.pacienteInputText = '';
    }
  }
  removePaciente(paciente: any): void {
    this.editForm.patchValue({ pacientes: [] });
  }
}
