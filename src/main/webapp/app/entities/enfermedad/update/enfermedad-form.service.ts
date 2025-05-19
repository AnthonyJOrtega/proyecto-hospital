import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IEnfermedad, NewEnfermedad } from '../enfermedad.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEnfermedad for edit and NewEnfermedadFormGroupInput for create.
 */
type EnfermedadFormGroupInput = IEnfermedad | PartialWithRequiredKeyOf<NewEnfermedad>;

type EnfermedadFormDefaults = Pick<NewEnfermedad, 'id' | 'informes'>;

type EnfermedadFormGroupContent = {
  id: FormControl<IEnfermedad['id'] | NewEnfermedad['id']>;
  nombre: FormControl<IEnfermedad['nombre']>;
  descripcion: FormControl<IEnfermedad['descripcion']>;
  informes: FormControl<IEnfermedad['informes']>;
};

export type EnfermedadFormGroup = FormGroup<EnfermedadFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EnfermedadFormService {
  createEnfermedadFormGroup(enfermedad: EnfermedadFormGroupInput = { id: null }): EnfermedadFormGroup {
    const enfermedadRawValue = {
      ...this.getFormDefaults(),
      ...enfermedad,
    };
    return new FormGroup<EnfermedadFormGroupContent>({
      id: new FormControl(
        { value: enfermedadRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombre: new FormControl(enfermedadRawValue.nombre),
      descripcion: new FormControl(enfermedadRawValue.descripcion),
      informes: new FormControl(enfermedadRawValue.informes ?? []),
    });
  }

  getEnfermedad(form: EnfermedadFormGroup): IEnfermedad | NewEnfermedad {
    return form.getRawValue() as IEnfermedad | NewEnfermedad;
  }

  resetForm(form: EnfermedadFormGroup, enfermedad: EnfermedadFormGroupInput): void {
    const enfermedadRawValue = { ...this.getFormDefaults(), ...enfermedad };
    form.reset(
      {
        ...enfermedadRawValue,
        id: { value: enfermedadRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EnfermedadFormDefaults {
    return {
      id: null,
      informes: [],
    };
  }
}
