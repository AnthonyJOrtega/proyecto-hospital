import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IReceta } from 'app/entities/receta/receta.model';
import { RecetaService } from 'app/entities/receta/service/receta.service';
import { IMedicamento } from '../medicamento.model';
import { MedicamentoService } from '../service/medicamento.service';
import { MedicamentoFormGroup, MedicamentoFormService } from './medicamento-form.service';

@Component({
  selector: 'jhi-medicamento-update',
  templateUrl: './medicamento-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MedicamentoUpdateComponent implements OnInit {
  isSaving = false;
  medicamento: IMedicamento | null = null;

  recetasSharedCollection: IReceta[] = [];

  protected medicamentoService = inject(MedicamentoService);
  protected medicamentoFormService = inject(MedicamentoFormService);
  protected recetaService = inject(RecetaService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MedicamentoFormGroup = this.medicamentoFormService.createMedicamentoFormGroup();

  compareReceta = (o1: IReceta | null, o2: IReceta | null): boolean => this.recetaService.compareReceta(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ medicamento }) => {
      this.medicamento = medicamento;
      if (medicamento) {
        this.updateForm(medicamento);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const medicamento = this.medicamentoFormService.getMedicamento(this.editForm);
    if (medicamento.id !== null) {
      this.subscribeToSaveResponse(this.medicamentoService.update(medicamento));
    } else {
      this.subscribeToSaveResponse(this.medicamentoService.create(medicamento));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMedicamento>>): void {
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

  protected updateForm(medicamento: IMedicamento): void {
    this.medicamento = medicamento;
    this.medicamentoFormService.resetForm(this.editForm, medicamento);

    this.recetasSharedCollection = this.recetaService.addRecetaToCollectionIfMissing<IReceta>(
      this.recetasSharedCollection,
      ...(medicamento.recetas ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.recetaService
      .query()
      .pipe(map((res: HttpResponse<IReceta[]>) => res.body ?? []))
      .pipe(
        map((recetas: IReceta[]) =>
          this.recetaService.addRecetaToCollectionIfMissing<IReceta>(recetas, ...(this.medicamento?.recetas ?? [])),
        ),
      )
      .subscribe((recetas: IReceta[]) => (this.recetasSharedCollection = recetas));
  }
}
