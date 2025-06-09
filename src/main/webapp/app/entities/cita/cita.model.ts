import dayjs from 'dayjs/esm';
import { IInforme } from 'app/entities/informe/informe.model';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { ITrabajador } from 'app/entities/trabajador/trabajador.model';
import { EstadoCita } from 'app/entities/enumerations/estado-cita.model';

export interface ICita {
  id: number;
  fechaCreacion?: dayjs.Dayjs | null;
  horaCreacion?: dayjs.Dayjs | null;
  estadoCita?: keyof typeof EstadoCita | null;
  estadoPaciente?: keyof typeof EstadoCita | null;
  observaciones?: string | null;
  informe?: Pick<IInforme, 'id'> | null;
  paciente?: Pick<IPaciente, 'id' | 'nombre' | 'apellido' | 'dni'> | null;
  trabajadors?: Pick<ITrabajador, 'id' | 'nombre' | 'apellido' | 'especialidads' | 'idUsuario'>[] | null;
}

export type NewCita = Omit<ICita, 'id'> & { id: null };
