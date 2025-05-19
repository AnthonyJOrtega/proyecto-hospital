import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITrabajador, NewTrabajador } from '../trabajador.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITrabajador for edit and NewTrabajadorFormGroupInput for create.
 */
type TrabajadorFormGroupInput = ITrabajador | PartialWithRequiredKeyOf<NewTrabajador>;

type TrabajadorFormDefaults = Pick<NewTrabajador, 'id' | 'disponibilidad' | 'especialidads' | 'citas' | 'pacientes' | 'direccions'>;

type TrabajadorFormGroupContent = {
  id: FormControl<ITrabajador['id'] | NewTrabajador['id']>;
  idUsuario: FormControl<ITrabajador['idUsuario']>;
  nombre: FormControl<ITrabajador['nombre']>;
  apellido: FormControl<ITrabajador['apellido']>;
  dni: FormControl<ITrabajador['dni']>;
  puesto: FormControl<ITrabajador['puesto']>;
  disponibilidad: FormControl<ITrabajador['disponibilidad']>;
  turno: FormControl<ITrabajador['turno']>;
  especialidads: FormControl<ITrabajador['especialidads']>;
  citas: FormControl<ITrabajador['citas']>;
  pacientes: FormControl<ITrabajador['pacientes']>;
  direccions: FormControl<ITrabajador['direccions']>;
};

export type TrabajadorFormGroup = FormGroup<TrabajadorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TrabajadorFormService {
  createTrabajadorFormGroup(trabajador: TrabajadorFormGroupInput = { id: null }): TrabajadorFormGroup {
    const trabajadorRawValue = {
      ...this.getFormDefaults(),
      ...trabajador,
    };
    return new FormGroup<TrabajadorFormGroupContent>({
      id: new FormControl(
        { value: trabajadorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      idUsuario: new FormControl(trabajadorRawValue.idUsuario),
      nombre: new FormControl(trabajadorRawValue.nombre),
      apellido: new FormControl(trabajadorRawValue.apellido),
      dni: new FormControl(trabajadorRawValue.dni),
      puesto: new FormControl(trabajadorRawValue.puesto),
      disponibilidad: new FormControl(trabajadorRawValue.disponibilidad),
      turno: new FormControl(trabajadorRawValue.turno),
      especialidads: new FormControl(trabajadorRawValue.especialidads ?? []),
      citas: new FormControl(trabajadorRawValue.citas ?? []),
      pacientes: new FormControl(trabajadorRawValue.pacientes ?? []),
      direccions: new FormControl(trabajadorRawValue.direccions ?? []),
    });
  }

  getTrabajador(form: TrabajadorFormGroup): ITrabajador | NewTrabajador {
    return form.getRawValue() as ITrabajador | NewTrabajador;
  }

  resetForm(form: TrabajadorFormGroup, trabajador: TrabajadorFormGroupInput): void {
    const trabajadorRawValue = { ...this.getFormDefaults(), ...trabajador };
    form.reset(
      {
        ...trabajadorRawValue,
        id: { value: trabajadorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TrabajadorFormDefaults {
    return {
      id: null,
      disponibilidad: false,
      especialidads: [],
      citas: [],
      pacientes: [],
      direccions: [],
    };
  }
}
