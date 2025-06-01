import dayjs from 'dayjs/esm';
import { ITrabajador } from 'app/entities/trabajador/trabajador.model';
import { IDireccion } from 'app/entities/direccion/direccion.model';
import { ICita } from '../cita/cita.model';

export interface IPaciente {
  id: number;
  nombre?: string | null;
  apellido?: string | null;
  dni?: string | null;
  seguroMedico?: string | null;
  fechaNacimiento?: dayjs.Dayjs | null;
  telefono?: string | null;
  trabajadors?: Pick<ITrabajador, 'id' | 'nombre' | 'apellido'>[] | null;
  direccions?: Pick<IDireccion, 'id' | 'calle' | 'codigoPostal' | 'numero' | 'ciudad' | 'pais'>[] | null;
  citas?: Pick<ICita, 'id' | 'trabajadors'>[] | null;
}

export type NewPaciente = Omit<IPaciente, 'id'> & { id: null };
