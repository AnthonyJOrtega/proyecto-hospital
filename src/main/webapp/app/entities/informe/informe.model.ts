import { IReceta } from 'app/entities/receta/receta.model';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { ITrabajador } from 'app/entities/trabajador/trabajador.model';
import { IEnfermedad } from 'app/entities/enfermedad/enfermedad.model';

export interface IInforme {
  id: number;
  fecha?: string | null;
  resumen?: string | null;
  receta?: Pick<IReceta, 'id'> | null;
  paciente?: Pick<IPaciente, 'id'> | null;
  trabajador?: Pick<ITrabajador, 'id'> | null;
  enfermedads?: Pick<IEnfermedad, 'id'>[] | null;
}

export type NewInforme = Omit<IInforme, 'id'> & { id: null };
