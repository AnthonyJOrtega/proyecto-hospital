import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IInforme } from 'app/entities/informe/informe.model';
import { InformeService } from 'app/entities/informe/service/informe.service';
import { IEnfermedad } from '../enfermedad.model';
import { EnfermedadService } from '../service/enfermedad.service';
import { EnfermedadFormGroup, EnfermedadFormService } from './enfermedad-form.service';

@Component({
  selector: 'jhi-enfermedad-update',
  templateUrl: './enfermedad-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EnfermedadUpdateComponent implements OnInit {
  isSaving = false;
  enfermedad: IEnfermedad | null = null;

  informesSharedCollection: IInforme[] = [];

  protected enfermedadService = inject(EnfermedadService);
  protected enfermedadFormService = inject(EnfermedadFormService);
  protected informeService = inject(InformeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EnfermedadFormGroup = this.enfermedadFormService.createEnfermedadFormGroup();

  compareInforme = (o1: IInforme | null, o2: IInforme | null): boolean => this.informeService.compareInforme(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ enfermedad }) => {
      this.enfermedad = enfermedad;
      if (enfermedad) {
        this.updateForm(enfermedad);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const enfermedad = this.enfermedadFormService.getEnfermedad(this.editForm);
    if (enfermedad.id !== null) {
      this.subscribeToSaveResponse(this.enfermedadService.update(enfermedad));
    } else {
      this.subscribeToSaveResponse(this.enfermedadService.create(enfermedad));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEnfermedad>>): void {
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

  protected updateForm(enfermedad: IEnfermedad): void {
    this.enfermedad = enfermedad;
    this.enfermedadFormService.resetForm(this.editForm, enfermedad);

    this.informesSharedCollection = this.informeService.addInformeToCollectionIfMissing<IInforme>(
      this.informesSharedCollection,
      ...(enfermedad.informes ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.informeService
      .query()
      .pipe(map((res: HttpResponse<IInforme[]>) => res.body ?? []))
      .pipe(
        map((informes: IInforme[]) =>
          this.informeService.addInformeToCollectionIfMissing<IInforme>(informes, ...(this.enfermedad?.informes ?? [])),
        ),
      )
      .subscribe((informes: IInforme[]) => (this.informesSharedCollection = informes));
  }
}
