import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';
import { ITrabajador } from 'app/entities/trabajador/trabajador.model';
import { TrabajadorService } from 'app/entities/trabajador/service/trabajador.service';
import { IMedicamento } from 'app/entities/medicamento/medicamento.model';
import { MedicamentoService } from 'app/entities/medicamento/service/medicamento.service';
import { RecetaService } from '../service/receta.service';
import { IReceta } from '../receta.model';
import { RecetaFormGroup, RecetaFormService } from './receta-form.service';

@Component({
  selector: 'jhi-receta-update',
  templateUrl: './receta-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RecetaUpdateComponent implements OnInit {
  isSaving = false;
  receta: IReceta | null = null;

  pacientesSharedCollection: IPaciente[] = [];
  trabajadorsSharedCollection: ITrabajador[] = [];
  medicamentosSharedCollection: IMedicamento[] = [];

  protected recetaService = inject(RecetaService);
  protected recetaFormService = inject(RecetaFormService);
  protected pacienteService = inject(PacienteService);
  protected trabajadorService = inject(TrabajadorService);
  protected medicamentoService = inject(MedicamentoService);
  protected activatedRoute = inject(ActivatedRoute);
  protected router = inject(Router);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RecetaFormGroup = this.recetaFormService.createRecetaFormGroup();

  comparePaciente = (o1: IPaciente | null, o2: IPaciente | null): boolean => this.pacienteService.comparePaciente(o1, o2);

  compareTrabajador = (o1: ITrabajador | null, o2: ITrabajador | null): boolean => this.trabajadorService.compareTrabajador(o1, o2);

  compareMedicamento = (o1: IMedicamento | null, o2: IMedicamento | null): boolean => this.medicamentoService.compareMedicamento(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ receta }) => {
      this.receta = receta;
      if (receta) {
        this.updateForm(receta);
      }
      // Asociar informe si viene por query param
      this.activatedRoute.queryParams.subscribe(params => {
        const informeId = params['informeId'];
        const pacienteId = params['pacienteId'];
        const trabajadorId = params['trabajadorId'];
        if (informeId) {
          this.editForm.patchValue({ informe: { id: +informeId } });
        }
        if (pacienteId) {
          this.editForm.patchValue({ paciente: { id: +pacienteId } });
        }
        if (trabajadorId) {
          this.editForm.patchValue({ trabajador: { id: +trabajadorId } });
        }
      });
      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const receta = this.recetaFormService.getReceta(this.editForm);
    if (receta.id !== null) {
      this.subscribeToSaveResponse(this.recetaService.update(receta));
    } else {
      this.subscribeToSaveResponse(this.recetaService.create(receta));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReceta>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.router.navigate(['/receta']);
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(receta: IReceta): void {
    this.receta = receta;
    this.recetaFormService.resetForm(this.editForm, receta);

    this.pacientesSharedCollection = this.pacienteService.addPacienteToCollectionIfMissing<IPaciente>(
      this.pacientesSharedCollection,
      receta.paciente,
    );
    this.trabajadorsSharedCollection = this.trabajadorService.addTrabajadorToCollectionIfMissing<ITrabajador>(
      this.trabajadorsSharedCollection,
      receta.trabajador,
    );
    this.medicamentosSharedCollection = this.medicamentoService.addMedicamentoToCollectionIfMissing<IMedicamento>(
      this.medicamentosSharedCollection,
      ...(receta.medicamentos ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.pacienteService
      .query()
      .pipe(map((res: HttpResponse<IPaciente[]>) => res.body ?? []))
      .pipe(
        map((pacientes: IPaciente[]) => this.pacienteService.addPacienteToCollectionIfMissing<IPaciente>(pacientes, this.receta?.paciente)),
      )
      .subscribe((pacientes: IPaciente[]) => (this.pacientesSharedCollection = pacientes));

    this.trabajadorService
      .query()
      .pipe(map((res: HttpResponse<ITrabajador[]>) => res.body ?? []))
      .pipe(
        map((trabajadors: ITrabajador[]) =>
          this.trabajadorService.addTrabajadorToCollectionIfMissing<ITrabajador>(trabajadors, this.receta?.trabajador),
        ),
      )
      .subscribe((trabajadors: ITrabajador[]) => (this.trabajadorsSharedCollection = trabajadors));

    this.medicamentoService
      .query()
      .pipe(map((res: HttpResponse<IMedicamento[]>) => res.body ?? []))
      .pipe(
        map((medicamentos: IMedicamento[]) =>
          this.medicamentoService.addMedicamentoToCollectionIfMissing<IMedicamento>(medicamentos, ...(this.receta?.medicamentos ?? [])),
        ),
      )
      .subscribe((medicamentos: IMedicamento[]) => (this.medicamentosSharedCollection = medicamentos));
  }

  medicamentoInputText = '';
  medicamentoSeleccionado: IMedicamento | null = null;

  addMedicamentoFromInput(): void {
    const input = this.medicamentoInputText?.trim().toLowerCase();
    if (!input) return;
    const medicamento = this.medicamentosSharedCollection.find(m => m.nombre?.toLowerCase() === input);
    if (medicamento) {
      this.medicamentoSeleccionado = medicamento;
      this.editForm.get('medicamentos')?.setValue([medicamento]);
    } else {
      this.editForm.get('medicamentos')?.setValue([]);
    }
    this.medicamentoInputText = '';
  }

  quitarMedicamento(): void {
    this.medicamentoSeleccionado = null;
    this.editForm.get('medicamentos')?.setValue([]);
  }
}
