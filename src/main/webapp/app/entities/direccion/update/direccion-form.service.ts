import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IDireccion, NewDireccion } from '../direccion.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDireccion for edit and NewDireccionFormGroupInput for create.
 */
type DireccionFormGroupInput = IDireccion | PartialWithRequiredKeyOf<NewDireccion>;

type DireccionFormDefaults = Pick<NewDireccion, 'id' | 'pacientes' | 'trabajadors'>;

type DireccionFormGroupContent = {
  id: FormControl<IDireccion['id'] | NewDireccion['id']>;
  pais: FormControl<IDireccion['pais']>;
  ciudad: FormControl<IDireccion['ciudad']>;
  localidad: FormControl<IDireccion['localidad']>;
  codigoPostal: FormControl<IDireccion['codigoPostal']>;
  calle: FormControl<IDireccion['calle']>;
  numero: FormControl<IDireccion['numero']>;
  pacientes: FormControl<IDireccion['pacientes']>;
  trabajadors: FormControl<IDireccion['trabajadors']>;
};

export type DireccionFormGroup = FormGroup<DireccionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DireccionFormService {
  createDireccionFormGroup(direccion: DireccionFormGroupInput = { id: null }): DireccionFormGroup {
    const direccionRawValue = {
      ...this.getFormDefaults(),
      ...direccion,
    };
    return new FormGroup<DireccionFormGroupContent>({
      id: new FormControl(
        { value: direccionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      pais: new FormControl(direccionRawValue.pais),
      ciudad: new FormControl(direccionRawValue.ciudad),
      localidad: new FormControl(direccionRawValue.localidad),
      codigoPostal: new FormControl(direccionRawValue.codigoPostal),
      calle: new FormControl(direccionRawValue.calle),
      numero: new FormControl(direccionRawValue.numero),
      pacientes: new FormControl(direccionRawValue.pacientes ?? []),
      trabajadors: new FormControl(direccionRawValue.trabajadors ?? []),
    });
  }

  getDireccion(form: DireccionFormGroup): IDireccion | NewDireccion {
    return form.getRawValue() as IDireccion | NewDireccion;
  }

  resetForm(form: DireccionFormGroup, direccion: DireccionFormGroupInput): void {
    const direccionRawValue = { ...this.getFormDefaults(), ...direccion };
    form.reset(
      {
        ...direccionRawValue,
        id: { value: direccionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DireccionFormDefaults {
    return {
      id: null,
      pacientes: [],
      trabajadors: [],
    };
  }
}
