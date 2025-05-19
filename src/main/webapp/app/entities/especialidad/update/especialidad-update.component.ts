import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITrabajador } from 'app/entities/trabajador/trabajador.model';
import { TrabajadorService } from 'app/entities/trabajador/service/trabajador.service';
import { IEspecialidad } from '../especialidad.model';
import { EspecialidadService } from '../service/especialidad.service';
import { EspecialidadFormGroup, EspecialidadFormService } from './especialidad-form.service';

@Component({
  selector: 'jhi-especialidad-update',
  templateUrl: './especialidad-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EspecialidadUpdateComponent implements OnInit {
  isSaving = false;
  especialidad: IEspecialidad | null = null;

  trabajadorsSharedCollection: ITrabajador[] = [];

  protected especialidadService = inject(EspecialidadService);
  protected especialidadFormService = inject(EspecialidadFormService);
  protected trabajadorService = inject(TrabajadorService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EspecialidadFormGroup = this.especialidadFormService.createEspecialidadFormGroup();

  compareTrabajador = (o1: ITrabajador | null, o2: ITrabajador | null): boolean => this.trabajadorService.compareTrabajador(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ especialidad }) => {
      this.especialidad = especialidad;
      if (especialidad) {
        this.updateForm(especialidad);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const especialidad = this.especialidadFormService.getEspecialidad(this.editForm);
    if (especialidad.id !== null) {
      this.subscribeToSaveResponse(this.especialidadService.update(especialidad));
    } else {
      this.subscribeToSaveResponse(this.especialidadService.create(especialidad));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEspecialidad>>): void {
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

  protected updateForm(especialidad: IEspecialidad): void {
    this.especialidad = especialidad;
    this.especialidadFormService.resetForm(this.editForm, especialidad);

    this.trabajadorsSharedCollection = this.trabajadorService.addTrabajadorToCollectionIfMissing<ITrabajador>(
      this.trabajadorsSharedCollection,
      ...(especialidad.trabajadors ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.trabajadorService
      .query()
      .pipe(map((res: HttpResponse<ITrabajador[]>) => res.body ?? []))
      .pipe(
        map((trabajadors: ITrabajador[]) =>
          this.trabajadorService.addTrabajadorToCollectionIfMissing<ITrabajador>(trabajadors, ...(this.especialidad?.trabajadors ?? [])),
        ),
      )
      .subscribe((trabajadors: ITrabajador[]) => (this.trabajadorsSharedCollection = trabajadors));
  }
}
