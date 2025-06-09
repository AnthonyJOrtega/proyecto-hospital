import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICita, NewCita } from '../cita.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICita for edit and NewCitaFormGroupInput for create.
 */
type CitaFormGroupInput = ICita | PartialWithRequiredKeyOf<NewCita>;

type CitaFormDefaults = Pick<NewCita, 'id' | 'trabajadors'>;

type CitaFormGroupContent = {
  id: FormControl<ICita['id'] | NewCita['id']>;
  fechaCreacion: FormControl<ICita['fechaCreacion']>;
  horaCreacion: FormControl<ICita['horaCreacion']>;
  estadoCita: FormControl<ICita['estadoCita']>;
  estadoPaciente: FormControl<ICita['estadoPaciente']>;
  observaciones: FormControl<ICita['observaciones']>;
  informe: FormControl<ICita['informe']>;
  paciente: FormControl<ICita['paciente']>;
  trabajadors: FormControl<ICita['trabajadors']>;
  pacienteString: FormControl<string | null>;
};

export type CitaFormGroup = FormGroup<CitaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CitaFormService {
  createCitaFormGroup(cita: CitaFormGroupInput = { id: null }): CitaFormGroup {
    const citaRawValue = {
      ...this.getFormDefaults(),
      ...cita,
    };
    return new FormGroup<CitaFormGroupContent>({
      id: new FormControl(
        { value: citaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fechaCreacion: new FormControl(citaRawValue.fechaCreacion),
      horaCreacion: new FormControl(citaRawValue.horaCreacion),
      estadoCita: new FormControl(citaRawValue.estadoCita),
      estadoPaciente: new FormControl(citaRawValue.estadoPaciente),
      observaciones: new FormControl(citaRawValue.observaciones),
      informe: new FormControl(citaRawValue.informe),
      paciente: new FormControl(citaRawValue.paciente),
      trabajadors: new FormControl(citaRawValue.trabajadors ?? []),
      pacienteString: new FormControl('', [Validators.required]),
    });
  }

  getCita(form: CitaFormGroup): ICita | NewCita {
    return form.getRawValue() as ICita | NewCita;
  }

  resetForm(form: CitaFormGroup, cita: CitaFormGroupInput): void {
    const citaRawValue = { ...this.getFormDefaults(), ...cita };
    form.reset(
      {
        ...citaRawValue,
        id: { value: citaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CitaFormDefaults {
    return {
      id: null,
      trabajadors: [],
    };
  }
}
