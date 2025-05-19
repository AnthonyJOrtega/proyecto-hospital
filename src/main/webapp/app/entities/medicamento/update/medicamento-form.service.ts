import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IMedicamento, NewMedicamento } from '../medicamento.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMedicamento for edit and NewMedicamentoFormGroupInput for create.
 */
type MedicamentoFormGroupInput = IMedicamento | PartialWithRequiredKeyOf<NewMedicamento>;

type MedicamentoFormDefaults = Pick<NewMedicamento, 'id' | 'recetas'>;

type MedicamentoFormGroupContent = {
  id: FormControl<IMedicamento['id'] | NewMedicamento['id']>;
  nombre: FormControl<IMedicamento['nombre']>;
  descripcion: FormControl<IMedicamento['descripcion']>;
  dosis: FormControl<IMedicamento['dosis']>;
  recetas: FormControl<IMedicamento['recetas']>;
};

export type MedicamentoFormGroup = FormGroup<MedicamentoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MedicamentoFormService {
  createMedicamentoFormGroup(medicamento: MedicamentoFormGroupInput = { id: null }): MedicamentoFormGroup {
    const medicamentoRawValue = {
      ...this.getFormDefaults(),
      ...medicamento,
    };
    return new FormGroup<MedicamentoFormGroupContent>({
      id: new FormControl(
        { value: medicamentoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombre: new FormControl(medicamentoRawValue.nombre),
      descripcion: new FormControl(medicamentoRawValue.descripcion),
      dosis: new FormControl(medicamentoRawValue.dosis),
      recetas: new FormControl(medicamentoRawValue.recetas ?? []),
    });
  }

  getMedicamento(form: MedicamentoFormGroup): IMedicamento | NewMedicamento {
    return form.getRawValue() as IMedicamento | NewMedicamento;
  }

  resetForm(form: MedicamentoFormGroup, medicamento: MedicamentoFormGroupInput): void {
    const medicamentoRawValue = { ...this.getFormDefaults(), ...medicamento };
    form.reset(
      {
        ...medicamentoRawValue,
        id: { value: medicamentoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MedicamentoFormDefaults {
    return {
      id: null,
      recetas: [],
    };
  }
}
