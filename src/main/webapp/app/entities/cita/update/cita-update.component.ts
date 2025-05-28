import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IInforme } from 'app/entities/informe/informe.model';
import { InformeService } from 'app/entities/informe/service/informe.service';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';
import { ITrabajador } from 'app/entities/trabajador/trabajador.model';
import { TrabajadorService } from 'app/entities/trabajador/service/trabajador.service';
import { EstadoCita } from 'app/entities/enumerations/estado-cita.model';
import { CitaService } from '../service/cita.service';
import { ICita } from '../cita.model';
import { CitaFormGroup, CitaFormService } from './cita-form.service';

@Component({
  selector: 'jhi-cita-update',
  templateUrl: './cita-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule, RouterModule],
})
export class CitaUpdateComponent implements OnInit {
  isSaving = false;
  cita: ICita | null = null;
  estadoCitaValues = Object.keys(EstadoCita);

  informesCollection: IInforme[] = [];
  pacientesSharedCollection: IPaciente[] = [];
  trabajadorsSharedCollection: ITrabajador[] = [];

  protected citaService = inject(CitaService);
  protected citaFormService = inject(CitaFormService);
  protected informeService = inject(InformeService);
  protected pacienteService = inject(PacienteService);
  protected trabajadorService = inject(TrabajadorService);
  protected activatedRoute = inject(ActivatedRoute);
  protected router = inject(Router);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CitaFormGroup = this.citaFormService.createCitaFormGroup();

  compareInforme = (o1: IInforme | null, o2: IInforme | null): boolean => this.informeService.compareInforme(o1, o2);

  comparePaciente = (o1: IPaciente | null, o2: IPaciente | null): boolean => this.pacienteService.comparePaciente(o1, o2);

  compareTrabajador = (o1: ITrabajador | null, o2: ITrabajador | null): boolean => this.trabajadorService.compareTrabajador(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cita }) => {
      this.cita = cita;
      this.editForm = this.citaFormService.createCitaFormGroup();
      document.body.classList.add('cita-update-view');
      if (cita) {
        this.updateForm(cita);
        // Si la cita tiene informe asociado, hacer patchValue del id
        if (cita.informe) {
          this.editForm.patchValue({ informe: { id: cita.informe.id } });
        }
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cita = this.citaFormService.getCita(this.editForm);
    if (cita.id !== null) {
      this.subscribeToSaveResponse(this.citaService.update(cita));
    } else {
      this.subscribeToSaveResponse(this.citaService.create(cita));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICita>>): void {
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

  protected updateForm(cita: ICita): void {
    this.cita = cita;
    this.citaFormService.resetForm(this.editForm, cita);

    this.informesCollection = this.informeService.addInformeToCollectionIfMissing<IInforme>(this.informesCollection, cita.informe);
    this.pacientesSharedCollection = this.pacienteService.addPacienteToCollectionIfMissing<IPaciente>(
      this.pacientesSharedCollection,
      cita.paciente,
    );
    this.trabajadorsSharedCollection = this.trabajadorService.addTrabajadorToCollectionIfMissing<ITrabajador>(
      this.trabajadorsSharedCollection,
      ...(cita.trabajadors ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.informeService
      .query({ 'citaId.specified': 'false' })
      .pipe(map((res: HttpResponse<IInforme[]>) => res.body ?? []))
      .pipe(map((informes: IInforme[]) => this.informeService.addInformeToCollectionIfMissing<IInforme>(informes, this.cita?.informe)))
      .subscribe((informes: IInforme[]) => (this.informesCollection = informes));

    this.pacienteService
      .query()
      .pipe(map((res: HttpResponse<IPaciente[]>) => res.body ?? []))
      .pipe(
        map((pacientes: IPaciente[]) => this.pacienteService.addPacienteToCollectionIfMissing<IPaciente>(pacientes, this.cita?.paciente)),
      )
      .subscribe((pacientes: IPaciente[]) => (this.pacientesSharedCollection = pacientes));

    this.trabajadorService
      .query()
      .pipe(map((res: HttpResponse<ITrabajador[]>) => res.body ?? []))
      .pipe(
        map((trabajadors: ITrabajador[]) =>
          this.trabajadorService.addTrabajadorToCollectionIfMissing<ITrabajador>(trabajadors, ...(this.cita?.trabajadors ?? [])),
        ),
      )
      .subscribe((trabajadors: ITrabajador[]) => (this.trabajadorsSharedCollection = trabajadors));
  }
  saveAndCreateInforme(): void {
    this.isSaving = true;
    const cita = this.citaFormService.getCita(this.editForm);
    const pacienteId = cita.paciente?.id;
    // Si tu modelo permite varios trabajadors, toma el primero (ajusta si es necesario)
    const trabajadorId = Array.isArray(cita.trabajadors) && cita.trabajadors.length > 0 ? cita.trabajadors[0].id : undefined;

    const onSuccess = (savedCita: ICita) => {
      this.router.navigate(['/informe/new'], {
        queryParams: {
          citaId: savedCita.id,
          pacienteId,
          trabajadorId,
        },
      });
    };
    if (cita.id !== null) {
      this.citaService
        .update(cita)
        .pipe(finalize(() => (this.isSaving = false)))
        .subscribe({
          next: res => onSuccess(res.body!),
          error: () => this.onSaveError(),
        });
    } else {
      this.citaService
        .create(cita)
        .pipe(finalize(() => (this.isSaving = false)))
        .subscribe({
          next: res => onSuccess(res.body!),
          error: () => this.onSaveError(),
        });
    }
  }
}
