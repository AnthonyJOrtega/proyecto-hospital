import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEspecialidad } from 'app/entities/especialidad/especialidad.model';
import { EspecialidadService } from 'app/entities/especialidad/service/especialidad.service';
import { ICita } from 'app/entities/cita/cita.model';
import { CitaService } from 'app/entities/cita/service/cita.service';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';
import { IDireccion } from 'app/entities/direccion/direccion.model';
import { DireccionService } from 'app/entities/direccion/service/direccion.service';
import { Puesto } from 'app/entities/enumerations/puesto.model';
import { Turno } from 'app/entities/enumerations/turno.model';
import { TrabajadorService } from '../service/trabajador.service';
import { ITrabajador } from '../trabajador.model';
import { TrabajadorFormGroup, TrabajadorFormService } from './trabajador-form.service';

@Component({
  selector: 'jhi-trabajador-update',
  templateUrl: './trabajador-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TrabajadorUpdateComponent implements OnInit {
  isSaving = false;
  trabajador: ITrabajador | null = null;
  puestoValues = Object.keys(Puesto);
  turnoValues = Object.keys(Turno);

  especialidadsSharedCollection: IEspecialidad[] = [];
  citasSharedCollection: ICita[] = [];
  pacientesSharedCollection: IPaciente[] = [];
  direccionsSharedCollection: IDireccion[] = [];

  protected trabajadorService = inject(TrabajadorService);
  protected trabajadorFormService = inject(TrabajadorFormService);
  protected especialidadService = inject(EspecialidadService);
  protected citaService = inject(CitaService);
  protected pacienteService = inject(PacienteService);
  protected direccionService = inject(DireccionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TrabajadorFormGroup = this.trabajadorFormService.createTrabajadorFormGroup();

  compareEspecialidad = (o1: IEspecialidad | null, o2: IEspecialidad | null): boolean =>
    this.especialidadService.compareEspecialidad(o1, o2);

  compareCita = (o1: ICita | null, o2: ICita | null): boolean => this.citaService.compareCita(o1, o2);

  comparePaciente = (o1: IPaciente | null, o2: IPaciente | null): boolean => this.pacienteService.comparePaciente(o1, o2);

  compareDireccion = (o1: IDireccion | null, o2: IDireccion | null): boolean => this.direccionService.compareDireccion(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trabajador }) => {
      this.trabajador = trabajador;
      if (trabajador) {
        this.updateForm(trabajador);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const trabajador = this.trabajadorFormService.getTrabajador(this.editForm);
    if (trabajador.id !== null) {
      this.subscribeToSaveResponse(this.trabajadorService.update(trabajador));
    } else {
      this.subscribeToSaveResponse(this.trabajadorService.create(trabajador));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrabajador>>): void {
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

  protected updateForm(trabajador: ITrabajador): void {
    this.trabajador = trabajador;
    this.trabajadorFormService.resetForm(this.editForm, trabajador);

    this.especialidadsSharedCollection = this.especialidadService.addEspecialidadToCollectionIfMissing<IEspecialidad>(
      this.especialidadsSharedCollection,
      ...(trabajador.especialidads ?? []),
    );
    this.citasSharedCollection = this.citaService.addCitaToCollectionIfMissing<ICita>(
      this.citasSharedCollection,
      ...(trabajador.citas ?? []),
    );
    this.pacientesSharedCollection = this.pacienteService.addPacienteToCollectionIfMissing<IPaciente>(
      this.pacientesSharedCollection,
      ...(trabajador.pacientes ?? []),
    );
    this.direccionsSharedCollection = this.direccionService.addDireccionToCollectionIfMissing<IDireccion>(
      this.direccionsSharedCollection,
      ...(trabajador.direccions ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.especialidadService
      .query()
      .pipe(map((res: HttpResponse<IEspecialidad[]>) => res.body ?? []))
      .pipe(
        map((especialidads: IEspecialidad[]) =>
          this.especialidadService.addEspecialidadToCollectionIfMissing<IEspecialidad>(
            especialidads,
            ...(this.trabajador?.especialidads ?? []),
          ),
        ),
      )
      .subscribe((especialidads: IEspecialidad[]) => (this.especialidadsSharedCollection = especialidads));

    this.citaService
      .query()
      .pipe(map((res: HttpResponse<ICita[]>) => res.body ?? []))
      .pipe(map((citas: ICita[]) => this.citaService.addCitaToCollectionIfMissing<ICita>(citas, ...(this.trabajador?.citas ?? []))))
      .subscribe((citas: ICita[]) => (this.citasSharedCollection = citas));

    this.pacienteService
      .query()
      .pipe(map((res: HttpResponse<IPaciente[]>) => res.body ?? []))
      .pipe(
        map((pacientes: IPaciente[]) =>
          this.pacienteService.addPacienteToCollectionIfMissing<IPaciente>(pacientes, ...(this.trabajador?.pacientes ?? [])),
        ),
      )
      .subscribe((pacientes: IPaciente[]) => (this.pacientesSharedCollection = pacientes));

    this.direccionService
      .query()
      .pipe(map((res: HttpResponse<IDireccion[]>) => res.body ?? []))
      .pipe(
        map((direccions: IDireccion[]) =>
          this.direccionService.addDireccionToCollectionIfMissing<IDireccion>(direccions, ...(this.trabajador?.direccions ?? [])),
        ),
      )
      .subscribe((direccions: IDireccion[]) => (this.direccionsSharedCollection = direccions));
  }
}
