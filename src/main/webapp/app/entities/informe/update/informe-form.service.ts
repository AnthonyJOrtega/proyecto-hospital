import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IInforme, NewInforme } from '../informe.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInforme for edit and NewInformeFormGroupInput for create.
 */
type InformeFormGroupInput = IInforme | PartialWithRequiredKeyOf<NewInforme>;

type InformeFormDefaults = Pick<NewInforme, 'id' | 'enfermedads'>;

type InformeFormGroupContent = {
  id: FormControl<IInforme['id'] | NewInforme['id']>;
  fecha: FormControl<IInforme['fecha']>;
  resumen: FormControl<IInforme['resumen']>;
  receta: FormControl<IInforme['receta']>;
  paciente: FormControl<IInforme['paciente']>;
  trabajador: FormControl<IInforme['trabajador']>;
  enfermedads: FormControl<IInforme['enfermedads']>;
};

export type InformeFormGroup = FormGroup<InformeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InformeFormService {
  createInformeFormGroup(informe: InformeFormGroupInput = { id: null }): InformeFormGroup {
    const informeRawValue = {
      ...this.getFormDefaults(),
      ...informe,
    };
    return new FormGroup<InformeFormGroupContent>({
      id: new FormControl(
        { value: informeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fecha: new FormControl(informeRawValue.fecha),
      resumen: new FormControl(informeRawValue.resumen),
      receta: new FormControl(informeRawValue.receta),
      paciente: new FormControl(informeRawValue.paciente),
      trabajador: new FormControl(informeRawValue.trabajador),
      enfermedads: new FormControl(informeRawValue.enfermedads ?? []),
    });
  }

  getInforme(form: InformeFormGroup): IInforme | NewInforme {
    return form.getRawValue() as IInforme | NewInforme;
  }

  resetForm(form: InformeFormGroup, informe: InformeFormGroupInput): void {
    const informeRawValue = { ...this.getFormDefaults(), ...informe };
    form.reset(
      {
        ...informeRawValue,
        id: { value: informeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): InformeFormDefaults {
    return {
      id: null,
      enfermedads: [],
    };
  }
}
