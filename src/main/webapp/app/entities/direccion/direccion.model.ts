import { IPaciente } from 'app/entities/paciente/paciente.model';
import { ITrabajador } from 'app/entities/trabajador/trabajador.model';

export interface IDireccion {
  id: number;
  pais?: string | null;
  ciudad?: string | null;
  localidad?: string | null;
  codigoPostal?: number | null;
  calle?: string | null;
  numero?: string | null;
  pacientes?: Pick<IPaciente, 'id'>[] | null;
  trabajadors?: Pick<ITrabajador, 'id'>[] | null;
}

export type NewDireccion = Omit<IDireccion, 'id'> & { id: null };
