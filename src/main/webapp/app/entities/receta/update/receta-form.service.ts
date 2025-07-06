import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IReceta, NewReceta } from '../receta.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReceta for edit and NewRecetaFormGroupInput for create.
 */
type RecetaFormGroupInput = IReceta | PartialWithRequiredKeyOf<NewReceta>;

type RecetaFormDefaults = Pick<NewReceta, 'id' | 'medicamentos'>;

type RecetaFormGroupContent = {
  id: FormControl<IReceta['id'] | NewReceta['id']>;
  fechaInicio: FormControl<IReceta['fechaInicio']>;
  fechaFin: FormControl<IReceta['fechaFin']>;
  instrucciones: FormControl<IReceta['instrucciones']>;
  paciente: FormControl<IReceta['paciente']>;
  trabajador: FormControl<IReceta['trabajador']>;
  medicamentos: FormControl<IReceta['medicamentos']>;
  informe: FormControl<IReceta['informe']>;
};

export type RecetaFormGroup = FormGroup<RecetaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RecetaFormService {
  createRecetaFormGroup(receta: RecetaFormGroupInput = { id: null }): RecetaFormGroup {
    const recetaRawValue = {
      ...this.getFormDefaults(),
      ...receta,
    };
    return new FormGroup<RecetaFormGroupContent>({
      id: new FormControl(
        { value: recetaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fechaInicio: new FormControl(recetaRawValue.fechaInicio, {
        nonNullable: true,
        validators: [Validators.required],
      }),
      fechaFin: new FormControl(recetaRawValue.fechaFin, {
        nonNullable: true,
        validators: [Validators.required],
      }),
      instrucciones: new FormControl(recetaRawValue.instrucciones, {
        nonNullable: true,
        validators: [Validators.required],
      }),
      paciente: new FormControl(recetaRawValue.paciente, {}),
      trabajador: new FormControl(recetaRawValue.trabajador, {}),
      medicamentos: new FormControl(recetaRawValue.medicamentos ?? [], {
        validators: [Validators.required],
        nonNullable: true,
      }),
      informe: new FormControl(recetaRawValue.informe),
    });
  }

  getReceta(form: RecetaFormGroup): IReceta | NewReceta {
    return form.getRawValue() as IReceta | NewReceta;
  }

  resetForm(form: RecetaFormGroup, receta: RecetaFormGroupInput): void {
    const recetaRawValue = { ...this.getFormDefaults(), ...receta };
    form.reset(
      {
        ...recetaRawValue,
        id: { value: recetaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RecetaFormDefaults {
    return {
      id: null,
      medicamentos: [],
    };
  }
}
