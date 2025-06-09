import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';
import { ITrabajador } from 'app/entities/trabajador/trabajador.model';
import { TrabajadorService } from 'app/entities/trabajador/service/trabajador.service';
import { IMedicamento } from 'app/entities/medicamento/medicamento.model';
import { MedicamentoService } from 'app/entities/medicamento/service/medicamento.service';
import { RecetaService } from '../service/receta.service';
import { IReceta } from '../receta.model';
import { RecetaFormGroup, RecetaFormService } from './receta-form.service';
import dayjs from 'dayjs/esm';
@Component({
  selector: 'jhi-receta-update',
  templateUrl: './receta-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RecetaUpdateComponent implements OnInit {
  isSaving = false;
  receta: IReceta | null = null;
  medicamentoInputText = '';
  medicamentoNoValido = false;
  medicamentoSeleccionado: IMedicamento | null = null;
  nuevaMedicamentoDosis = '';
  nuevoMedicamentoCreado = false;

  pacientesSharedCollection: IPaciente[] = [];
  trabajadorsSharedCollection: ITrabajador[] = [];
  medicamentosSharedCollection: IMedicamento[] = [];
  medicamentosSeleccionados: IMedicamento[] = [];

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
        const fechaInicio = params['fechaInicio'];
        if (informeId) {
          this.editForm.patchValue({ informe: { id: +informeId } });
        }
        if (pacienteId) {
          this.editForm.patchValue({ paciente: { id: +pacienteId } });
        }
        if (trabajadorId) {
          this.editForm.patchValue({ trabajador: { id: +trabajadorId } });
        }
        if (fechaInicio) {
          this.editForm.patchValue({ fechaInicio: dayjs(fechaInicio) });
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
    this.medicamentosSeleccionados = receta.medicamentos ? [...receta.medicamentos] : [];
    this.editForm.get('medicamentos')?.setValue(this.medicamentosSeleccionados);
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

  addMedicamentoFromInput(): void {
    const input = this.medicamentoInputText.trim();
    const encontrado = this.medicamentosSharedCollection.find(m => `${m.nombre} (${m.dosis})` === input);

    if (encontrado && !this.medicamentosSeleccionados.some(m => m.id === encontrado.id)) {
      this.medicamentosSeleccionados.push(encontrado);
      this.editForm.get('medicamentos')?.setValue(this.medicamentosSeleccionados);
      this.medicamentoNoValido = false;
      this.nuevaMedicamentoDosis = '';
    } else if (!encontrado) {
      this.medicamentoNoValido = !!this.medicamentoInputText;
    }
    this.medicamentoInputText = '';
  }
  nuevaMedicamentoDescripcion = '';
  crearNuevoMedicamento(): void {
    const nombre = this.medicamentoInputText?.trim();
    const dosis = this.nuevaMedicamentoDosis?.trim();
    const descripcion = this.nuevaMedicamentoDescripcion?.trim();
    if (!nombre || !dosis) return;

    this.medicamentoService.create({ id: null, nombre, dosis, descripcion }).subscribe({
      next: response => {
        const nuevo = response.body!;
        this.medicamentosSharedCollection.push(nuevo);
        this.medicamentoSeleccionado = nuevo;
        this.editForm.get('medicamentos')?.setValue([nuevo]);
        this.nuevoMedicamentoCreado = true;
        this.medicamentoNoValido = false;
        this.nuevaMedicamentoDosis = '';
        this.nuevaMedicamentoDescripcion = '';
        setTimeout(() => (this.nuevoMedicamentoCreado = false), 4000);
      },
      error: () => {
        // Manejo de error si lo necesitas
      },
    });
  }

  quitarMedicamento(index: number): void {
    this.medicamentosSeleccionados.splice(index, 1);
    this.editForm.get('medicamentos')?.setValue(this.medicamentosSeleccionados);
  }
  onMedicamentoInput(): void {
    const value = this.editForm.get('medicamento')?.value;

    if (!value) {
      this.medicamentoSeleccionado = null;
      this.medicamentoNoValido = false;
      return;
    }

    // Busca coincidencia exacta con nombre + dosis
    const encontrada = this.medicamentosSharedCollection.find(m => `${m.nombre} (${m.dosis})` === value);

    if (encontrada) {
      this.medicamentoSeleccionado = encontrada;
      this.editForm.setControl('medicamentos', new FormControl([encontrada])); // Opcional: para guardar
      this.medicamentoNoValido = false;
    } else {
      this.medicamentoSeleccionado = null;
      this.editForm.setControl('medicamentos', new FormControl([]));
      this.medicamentoNoValido = true;
    }
  }
}
