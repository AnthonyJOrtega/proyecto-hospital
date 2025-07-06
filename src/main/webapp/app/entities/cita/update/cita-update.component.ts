import { Component, Input, OnInit, Optional, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormControl, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';

import { IInforme } from 'app/entities/informe/informe.model';
import { InformeService } from 'app/entities/informe/service/informe.service';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';
import { ITrabajador } from 'app/entities/trabajador/trabajador.model';
import { TrabajadorService } from 'app/entities/trabajador/service/trabajador.service';
import { EstadoCita } from 'app/entities/enumerations/estado-cita.model';
import { CitaService } from '../service/cita.service';
import { ICita, NewCita } from '../cita.model';
import { CitaFormGroup, CitaFormService } from './cita-form.service';
import { NgbActiveModal, NgbTimepickerModule } from '@ng-bootstrap/ng-bootstrap';
import dayjs from 'dayjs/esm';

@Component({
  selector: 'jhi-cita-update',
  templateUrl: './cita-update.component.html',
  styleUrls: ['./cita-update.component.scss'],
  imports: [SharedModule, FormsModule, ReactiveFormsModule, RouterModule, NgbTimepickerModule],
})
export class CitaUpdateComponent implements OnInit {
  @Input() citaId?: number;
  @Input() fecha?: string;

  isSaving = false;
  cita: ICita | null = null;
  estadoCitaValues = ['PENDIENTE', 'FINALIZADO', 'CANCELADO'];
  estadoPacienteValues = ['URGENTE', 'CONTROLMEDICO'];
  pacienteSeleccionado: IPaciente | null = null;
  pacienteNoValido = false;
  trabajadorInputText = '';
  trabajadorSeleccionado: ITrabajador | null = null;
  trabajadorNoValido = false;
  franjaOcupada = false;

  informesCollection: IInforme[] = [];
  pacientesSharedCollection: IPaciente[] = [];
  trabajadorsSharedCollection: ITrabajador[] = [];
  medicosSharedCollection: ITrabajador[] = [];
  public isModal: boolean = false;
  citaDuplicada = false;
  pacienteDuplicado = false;
  nuevoPacienteCreado = false;
  pacienteCreado = false;
  minFecha = { year: new Date().getFullYear(), month: new Date().getMonth() + 1, day: new Date().getDate() };
  esNuevaCita = true;
  palabrasObservaciones = 0;

  protected citaService = inject(CitaService);
  protected citaFormService = inject(CitaFormService);
  protected informeService = inject(InformeService);
  protected pacienteService = inject(PacienteService);
  protected trabajadorService = inject(TrabajadorService);
  protected activatedRoute = inject(ActivatedRoute);
  protected router = inject(Router);

  constructor(@Optional() public activeModal: NgbActiveModal) {
    this.isModal = !!activeModal;
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CitaFormGroup = this.citaFormService.createCitaFormGroup();

  compareInforme = (o1: IInforme | null, o2: IInforme | null): boolean => this.informeService.compareInforme(o1, o2);

  comparePaciente = (o1: IPaciente | null, o2: IPaciente | null): boolean => this.pacienteService.comparePaciente(o1, o2);

  compareTrabajador = (o1: ITrabajador | null, o2: ITrabajador | null): boolean => this.trabajadorService.compareTrabajador(o1, o2);
  pacienteStringControl!: FormControl<any>;

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cita }) => {
      this.esNuevaCita = !cita?.id; // para el calendario si es una nueva cita no poder elegir dias anteriores
      this.cita = cita;
      this.editForm = this.citaFormService.createCitaFormGroup();
      document.body.classList.add('cita-update-view');
      // Inicializa el control pacienteString si es necesario
      if (!this.editForm.get('pacienteString')) {
        this.editForm.addControl('pacienteString', new FormControl('', [Validators.required]));
      }
      if (!this.editForm.get('horaCreacion')) {
        this.editForm.addControl('horaCreacion', new FormControl<dayjs.Dayjs | null | undefined>(null, [Validators.required]));
      }
      if (!this.editForm.get('id')?.value) {
        this.editForm.get('estadoCita')?.setValue('PENDIENTE');
      }
      if (this.citaId) {
        this.citaService.find(this.citaId).subscribe(res => {
          this.cita = res.body;
          if (this.cita) {
            this.updateForm(this.cita); // Actualiza el formulario con los datos de la cita
          }
          this.loadRelationshipsOptions(); // Carga las relaciones necesarias
        });
      } else {
        const queryFecha = this.activatedRoute.snapshot.queryParamMap.get('fecha');
        const fecha = this.fecha ?? queryFecha;
        if (fecha) {
          this.editForm.patchValue({ fechaCreacion: dayjs(fecha) });
        }
        this.loadRelationshipsOptions();
      }
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
    if (this.isModal) {
      this.activeModal.dismiss();
    } else {
      window.history.back();
    }
  }

  save(): void {
    this.validatePacienteSelection();

    if (this.editForm.invalid || !this.editForm.get('paciente')?.value) {
      return;
    }

    this.isSaving = true;
    const cita = this.citaFormService.getCita(this.editForm);

    const result = cita.id ? this.citaService.update(cita) : this.citaService.create(cita as NewCita);
    result.subscribe({
      next: () => {
        if (this.isModal) {
          this.activeModal.close('saved'); // <-- Esto refresca el calendario
        } else {
          this.previousState();
        }
      },
      error: err => {
        this.isSaving = false;
        // Detecta el error de franja ocupada
        if (err?.error?.type === 'franjaocupada' || err?.error?.message === 'error.franjaocupada') {
          this.franjaOcupada = true;
          this.citaDuplicada = false;
        } else if (err?.error?.type === 'duplicada' || err?.error?.message === 'error.duplicada') {
          this.citaDuplicada = true;
          this.franjaOcupada = false;
        } else {
          this.citaDuplicada = false;
          this.franjaOcupada = false;
        }
      },
    });
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICita>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    if (this.isModal) {
      this.activeModal.close('saved');
    } else {
      this.previousState();
    }
  }
  cancel(): void {
    if (this.isModal) {
      this.activeModal.dismiss();
    } else {
      this.previousState();
    }
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
    // --- Añade esto ---
    // Rellena el campo auxiliar de paciente
    if (cita.paciente) {
      this.editForm.get('pacienteString')?.setValue(`${cita.paciente.nombre} ${cita.paciente.apellido} - DNI: ${cita.paciente.dni}`);
    } else {
      this.editForm.get('pacienteString')?.setValue('');
    }

    // Rellena el campo auxiliar de trabajador
    if (cita.trabajadors && cita.trabajadors.length > 0) {
      const trabajador = cita.trabajadors[0];
      this.trabajadorSeleccionado = trabajador;
      this.trabajadorInputText = `${trabajador.nombre} ${trabajador.apellido} (${this.getEspecialidadesString(trabajador)}) --- ID usuario: ${trabajador.idUsuario}`;
    }
  }

  protected loadRelationshipsOptions(): void {
    this.informeService
      .query({ 'citaId.specified': 'false' })
      .pipe(map((res: HttpResponse<IInforme[]>) => res.body ?? []))
      .pipe(map((informes: IInforme[]) => this.informeService.addInformeToCollectionIfMissing<IInforme>(informes, this.cita?.informe)))
      .subscribe((informes: IInforme[]) => (this.informesCollection = informes));

    this.pacienteService
      .query({ size: 10000 })
      .pipe(map((res: HttpResponse<IPaciente[]>) => res.body ?? []))
      .pipe(
        map((pacientes: IPaciente[]) => this.pacienteService.addPacienteToCollectionIfMissing<IPaciente>(pacientes, this.cita?.paciente)),
      )
      .subscribe((pacientes: IPaciente[]) => (this.pacientesSharedCollection = pacientes));

    this.trabajadorService
      .query({ size: 10000 })
      .pipe(map((res: HttpResponse<ITrabajador[]>) => res.body ?? []))
      .pipe(
        map((trabajadors: ITrabajador[]) =>
          this.trabajadorService.addTrabajadorToCollectionIfMissing<ITrabajador>(trabajadors, ...(this.cita?.trabajadors ?? [])),
        ),
      )
      .subscribe((trabajadors: ITrabajador[]) => {
        this.trabajadorsSharedCollection = trabajadors;
        this.medicosSharedCollection = trabajadors.filter(t => t.puesto === 'MEDICO');
      });
  }
  saveAndCreateInforme(): void {
    this.isSaving = true;
    const cita = this.citaFormService.getCita(this.editForm);
    const pacienteId = cita.paciente?.id;
    // Si tu modelo permite varios trabajadors, toma el primero (ajusta si es necesario)
    const trabajadorId = Array.isArray(cita.trabajadors) && cita.trabajadors.length > 0 ? cita.trabajadors[0].id : undefined;

    const onSuccess = (savedCita: ICita) => {
      // Cierra el modal si está en modo modal
      if (this.isModal && this.activeModal) {
        this.activeModal.close('saved');
      }
      this.router.navigate(['/informe/new'], {
        queryParams: {
          citaId: savedCita.id,
          pacienteId,
          trabajadorId,
          fecha: savedCita.fechaCreacion?.format('YYYY-MM-DD') || dayjs().format('YYYY-MM-DD'),
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
  // Método para validar la selección del paciente con chip list verde y quitarlo con el método quitarPaciente
  validatePacienteSelection(): void {
    const nombreCompleto = this.editForm.get('pacienteString')?.value;
    const paciente = this.pacientesSharedCollection.find(p => p.nombre + ' ' + p.apellido + ' - DNI: ' + p.dni === nombreCompleto);
    if (paciente) {
      this.pacienteSeleccionado = paciente;
      this.editForm.patchValue({ paciente: paciente });
      this.pacienteNoValido = false;
    } else {
      this.pacienteSeleccionado = null;
      this.editForm.patchValue({ paciente: null });
      this.pacienteNoValido = true;
    }
  }

  quitarPaciente(): void {
    this.pacienteSeleccionado = null;
    this.editForm.get('paciente')?.setValue(null);
    this.editForm.get('pacienteString')?.setValue('');
    this.pacienteNoValido = false;
  }

  // Método para manejar el input de trabajador con chip list azul y quitarlo con el metodo quitarTrabajador
  addTrabajadorFromInput(): void {
    const input = this.trabajadorInputText?.trim().toLowerCase();
    if (!input) return;
    const trabajador = this.medicosSharedCollection.find(
      t =>
        (t.nombre + ' ' + t.apellido + ' ' + '(' + this.getEspecialidadesString(t) + ') --- ID usuario: ' + t.idUsuario).toLowerCase() ===
        input,
    );
    if (trabajador) {
      this.trabajadorSeleccionado = trabajador;
      this.editForm.get('trabajadors')?.setValue([trabajador]);
      this.trabajadorNoValido = false;
    } else {
      this.trabajadorNoValido = true;
      this.editForm.get('trabajadors')?.setValue([]);
    }
    this.trabajadorInputText = '';
  }

  quitarTrabajador(): void {
    this.trabajadorSeleccionado = null;
    this.trabajadorInputText = '';
    this.editForm.get('trabajadors')?.setValue([]);
    this.trabajadorNoValido = false;
  }
  // Método para filtrar los trabajadores por puesto 'MEDICO'
  get medicos(): ITrabajador[] {
    return this.trabajadorsSharedCollection.filter(t => t.puesto === 'MEDICO');
  }
  getEspecialidadesString(trabajador: ITrabajador): string {
    return (trabajador.especialidads ?? []).map(e => e.nombre).join(', ');
  }

  //METODOS PARA FILTRAR POR ESPECIALIDAD
  especialidadSeleccionada: string | null = null; // variable del id de la especialidad seleccionada

  get especialidades(): string[] {
    // Devuelve todas las especialidades únicas de los médicos
    const all = this.medicosSharedCollection.flatMap(m => m.especialidads ?? []);
    const unique = Array.from(new Set(all.map(e => e.nombre).filter((n): n is string => typeof n === 'string')));
    return unique;
  }
  get medicosFiltrados(): ITrabajador[] {
    if (!this.especialidadSeleccionada) return this.medicosSharedCollection;
    return this.medicosSharedCollection.filter(m => (m.especialidads ?? []).some(e => e.nombre === this.especialidadSeleccionada));
  }

  onEspecialidadChange(event: Event): void {
    const select = event.target as HTMLSelectElement;
    const value = select.value;
    this.especialidadSeleccionada = value || null;
    this.trabajadorSeleccionado = null;
    this.editForm.get('trabajadors')?.setValue([]);
    this.trabajadorNoValido = false;
  }
  onTrabajadorSelect(): void {
    if (this.trabajadorSeleccionado) {
      this.editForm.get('trabajadors')?.setValue([this.trabajadorSeleccionado]);
      this.trabajadorNoValido = false;
    } else {
      this.editForm.get('trabajadors')?.setValue([]);
      this.trabajadorNoValido = true;
    }
  }
  //metodo para crear un nuevo paciente sino existe
  crearNuevoPaciente(): void {
    const inputValue = this.editForm.get('pacienteString')?.value?.trim();
    if (!inputValue) {
      return;
    }

    // Comprobar duplicados por nombre y apellido
    const isDuplicate = this.pacientesSharedCollection.some(
      paciente => `${paciente.nombre} ${paciente.apellido}`.toLowerCase() === inputValue.toLowerCase(),
    );

    if (isDuplicate) {
      this.pacienteDuplicado = true;
      return; // No permitimos crear un paciente duplicado
    }

    // Separar nombre y apellidos
    const [nombre, ...apellidoParts] = inputValue.split(' ');
    const apellido = apellidoParts.join(' ');

    const nuevoPaciente = {
      id: null,
      nombre: nombre.trim(),
      apellido: apellido.trim(),
      dni: '',
      seguroMedico: '',
      fechaNacimiento: null,
      telefono: '',
    } as const; // Usamos 'as const' para inferir el tipo literal

    this.pacienteService.create(nuevoPaciente).subscribe({
      next: response => {
        // Agrega el nuevo paciente a la lista y lo selecciona
        this.pacientesSharedCollection.push(response.body!);
        this.pacienteSeleccionado = response.body!;
        this.editForm.get('paciente')?.setValue(response.body);
        this.editForm.get('pacienteString')?.setValue(`${response.body?.nombre} ${response.body?.apellido} - DNI: ${response.body?.dni}`);
        this.editForm.get('pacienteString')?.setErrors(null);
        this.pacienteNoValido = false;
        this.nuevoPacienteCreado = true; // Mostrar mensaje de éxito
        setTimeout(() => (this.nuevoPacienteCreado = false), 5000); // Ocultar mensaje después de 5 segundos
      },
      error: () => {
        this.editForm.get('paciente')?.setValue(null);
        this.editForm.get('pacienteString')?.setErrors({ invalidPaciente: true });
        this.pacienteNoValido = true;
      },
    });
  }

  limitWords(event: Event): void {
    const control = this.editForm.get('observaciones');
    if (!control) return;

    let value = control.value || '';
    let words = value
      .trim()
      .split(/\s+/)
      .filter(w => w);

    this.palabrasObservaciones = words.length;

    if (words.length > 150) {
      // Recorta a 150 palabras
      const truncated = words.slice(0, 150).join(' ');
      control.setValue(truncated, { emitEvent: false });
      this.palabrasObservaciones = 150;
    }
  }

  // Bloquea cualquier escritura adicional si ya se alcanzó el límite
  onKeyDown(event: KeyboardEvent): void {
    const control = this.editForm.get('observaciones');
    if (!control) return;

    const value = control.value || '';
    const words = value
      .trim()
      .split(/\s+/)
      .filter(w => w);
    this.palabrasObservaciones = words.length;

    if (words.length >= 150 && !this.isAllowedKey(event)) {
      event.preventDefault(); // Bloquea la entrada
    }
  }

  // Permite ciertas teclas incluso al llegar al límite (flechas, borrar, etc.)
  private isAllowedKey(event: KeyboardEvent): boolean {
    const allowedKeys = ['Backspace', 'Delete', 'ArrowLeft', 'ArrowRight', 'ArrowUp', 'ArrowDown', 'Home', 'End', 'Tab'];
    return allowedKeys.includes(event.key);
  }
}
