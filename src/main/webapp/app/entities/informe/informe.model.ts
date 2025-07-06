import { IReceta } from 'app/entities/receta/receta.model';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { ITrabajador } from 'app/entities/trabajador/trabajador.model';
import { IEnfermedad } from 'app/entities/enfermedad/enfermedad.model';
import { ICita } from '../cita/cita.model';

export interface IInforme {
  id: number;
  fecha?: string | null;
  resumen?: string | null;
  receta?: Pick<IReceta, 'id'> | null;
  paciente?: Pick<IPaciente, 'id' | 'nombre' | 'apellido' | 'dni'> | null;
  trabajador?: Pick<ITrabajador, 'id' | 'nombre' | 'apellido' | 'puesto' | 'especialidads' | 'idUsuario'> | null;
  enfermedads?: Pick<IEnfermedad, 'id' | 'nombre' | 'descripcion'>[] | null;
  cita?: Pick<ICita, 'id'> | null;
}

export type NewInforme = Omit<IInforme, 'id'> & { id: null };
