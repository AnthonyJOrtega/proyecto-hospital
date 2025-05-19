import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPaciente, NewPaciente } from '../paciente.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPaciente for edit and NewPacienteFormGroupInput for create.
 */
type PacienteFormGroupInput = IPaciente | PartialWithRequiredKeyOf<NewPaciente>;

type PacienteFormDefaults = Pick<NewPaciente, 'id' | 'trabajadors' | 'direccions'>;

type PacienteFormGroupContent = {
  id: FormControl<IPaciente['id'] | NewPaciente['id']>;
  nombre: FormControl<IPaciente['nombre']>;
  apellido: FormControl<IPaciente['apellido']>;
  dni: FormControl<IPaciente['dni']>;
  seguroMedico: FormControl<IPaciente['seguroMedico']>;
  fechaNacimiento: FormControl<IPaciente['fechaNacimiento']>;
  telefono: FormControl<IPaciente['telefono']>;
  trabajadors: FormControl<IPaciente['trabajadors']>;
  direccions: FormControl<IPaciente['direccions']>;
};

export type PacienteFormGroup = FormGroup<PacienteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PacienteFormService {
  createPacienteFormGroup(paciente: PacienteFormGroupInput = { id: null }): PacienteFormGroup {
    const pacienteRawValue = {
      ...this.getFormDefaults(),
      ...paciente,
    };
    return new FormGroup<PacienteFormGroupContent>({
      id: new FormControl(
        { value: pacienteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombre: new FormControl(pacienteRawValue.nombre),
      apellido: new FormControl(pacienteRawValue.apellido),
      dni: new FormControl(pacienteRawValue.dni),
      seguroMedico: new FormControl(pacienteRawValue.seguroMedico),
      fechaNacimiento: new FormControl(pacienteRawValue.fechaNacimiento),
      telefono: new FormControl(pacienteRawValue.telefono),
      trabajadors: new FormControl(pacienteRawValue.trabajadors ?? []),
      direccions: new FormControl(pacienteRawValue.direccions ?? []),
    });
  }

  getPaciente(form: PacienteFormGroup): IPaciente | NewPaciente {
    return form.getRawValue() as IPaciente | NewPaciente;
  }

  resetForm(form: PacienteFormGroup, paciente: PacienteFormGroupInput): void {
    const pacienteRawValue = { ...this.getFormDefaults(), ...paciente };
    form.reset(
      {
        ...pacienteRawValue,
        id: { value: pacienteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PacienteFormDefaults {
    return {
      id: null,
      trabajadors: [],
      direccions: [],
    };
  }
}
