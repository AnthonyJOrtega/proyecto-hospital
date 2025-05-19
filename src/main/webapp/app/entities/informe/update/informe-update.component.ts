import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IReceta } from 'app/entities/receta/receta.model';
import { RecetaService } from 'app/entities/receta/service/receta.service';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';
import { ITrabajador } from 'app/entities/trabajador/trabajador.model';
import { TrabajadorService } from 'app/entities/trabajador/service/trabajador.service';
import { IEnfermedad } from 'app/entities/enfermedad/enfermedad.model';
import { EnfermedadService } from 'app/entities/enfermedad/service/enfermedad.service';
import { InformeService } from '../service/informe.service';
import { IInforme } from '../informe.model';
import { InformeFormGroup, InformeFormService } from './informe-form.service';

@Component({
  selector: 'jhi-informe-update',
  templateUrl: './informe-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class InformeUpdateComponent implements OnInit {
  isSaving = false;
  informe: IInforme | null = null;

  recetasCollection: IReceta[] = [];
  pacientesSharedCollection: IPaciente[] = [];
  trabajadorsSharedCollection: ITrabajador[] = [];
  enfermedadsSharedCollection: IEnfermedad[] = [];

  protected informeService = inject(InformeService);
  protected informeFormService = inject(InformeFormService);
  protected recetaService = inject(RecetaService);
  protected pacienteService = inject(PacienteService);
  protected trabajadorService = inject(TrabajadorService);
  protected enfermedadService = inject(EnfermedadService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InformeFormGroup = this.informeFormService.createInformeFormGroup();

  compareReceta = (o1: IReceta | null, o2: IReceta | null): boolean => this.recetaService.compareReceta(o1, o2);

  comparePaciente = (o1: IPaciente | null, o2: IPaciente | null): boolean => this.pacienteService.comparePaciente(o1, o2);

  compareTrabajador = (o1: ITrabajador | null, o2: ITrabajador | null): boolean => this.trabajadorService.compareTrabajador(o1, o2);

  compareEnfermedad = (o1: IEnfermedad | null, o2: IEnfermedad | null): boolean => this.enfermedadService.compareEnfermedad(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ informe }) => {
      this.informe = informe;
      if (informe) {
        this.updateForm(informe);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const informe = this.informeFormService.getInforme(this.editForm);
    if (informe.id !== null) {
      this.subscribeToSaveResponse(this.informeService.update(informe));
    } else {
      this.subscribeToSaveResponse(this.informeService.create(informe));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInforme>>): void {
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

  protected updateForm(informe: IInforme): void {
    this.informe = informe;
    this.informeFormService.resetForm(this.editForm, informe);

    this.recetasCollection = this.recetaService.addRecetaToCollectionIfMissing<IReceta>(this.recetasCollection, informe.receta);
    this.pacientesSharedCollection = this.pacienteService.addPacienteToCollectionIfMissing<IPaciente>(
      this.pacientesSharedCollection,
      informe.paciente,
    );
    this.trabajadorsSharedCollection = this.trabajadorService.addTrabajadorToCollectionIfMissing<ITrabajador>(
      this.trabajadorsSharedCollection,
      informe.trabajador,
    );
    this.enfermedadsSharedCollection = this.enfermedadService.addEnfermedadToCollectionIfMissing<IEnfermedad>(
      this.enfermedadsSharedCollection,
      ...(informe.enfermedads ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.recetaService
      .query({ 'informeId.specified': 'false' })
      .pipe(map((res: HttpResponse<IReceta[]>) => res.body ?? []))
      .pipe(map((recetas: IReceta[]) => this.recetaService.addRecetaToCollectionIfMissing<IReceta>(recetas, this.informe?.receta)))
      .subscribe((recetas: IReceta[]) => (this.recetasCollection = recetas));

    this.pacienteService
      .query()
      .pipe(map((res: HttpResponse<IPaciente[]>) => res.body ?? []))
      .pipe(
        map((pacientes: IPaciente[]) =>
          this.pacienteService.addPacienteToCollectionIfMissing<IPaciente>(pacientes, this.informe?.paciente),
        ),
      )
      .subscribe((pacientes: IPaciente[]) => (this.pacientesSharedCollection = pacientes));

    this.trabajadorService
      .query()
      .pipe(map((res: HttpResponse<ITrabajador[]>) => res.body ?? []))
      .pipe(
        map((trabajadors: ITrabajador[]) =>
          this.trabajadorService.addTrabajadorToCollectionIfMissing<ITrabajador>(trabajadors, this.informe?.trabajador),
        ),
      )
      .subscribe((trabajadors: ITrabajador[]) => (this.trabajadorsSharedCollection = trabajadors));

    this.enfermedadService
      .query()
      .pipe(map((res: HttpResponse<IEnfermedad[]>) => res.body ?? []))
      .pipe(
        map((enfermedads: IEnfermedad[]) =>
          this.enfermedadService.addEnfermedadToCollectionIfMissing<IEnfermedad>(enfermedads, ...(this.informe?.enfermedads ?? [])),
        ),
      )
      .subscribe((enfermedads: IEnfermedad[]) => (this.enfermedadsSharedCollection = enfermedads));
  }
}
