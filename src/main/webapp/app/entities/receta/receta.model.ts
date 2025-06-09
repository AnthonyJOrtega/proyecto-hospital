import dayjs from 'dayjs/esm';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { ITrabajador } from 'app/entities/trabajador/trabajador.model';
import { IMedicamento } from 'app/entities/medicamento/medicamento.model';
import { IInforme } from '../informe/informe.model';

export interface IReceta {
  id: number;
  fechaInicio?: dayjs.Dayjs | null;
  fechaFin?: dayjs.Dayjs | null;
  instrucciones?: string | null;
  paciente?: Pick<IPaciente, 'id' | 'nombre' | 'apellido' | 'dni'> | null;
  trabajador?: Pick<ITrabajador, 'id' | 'nombre' | 'apellido' | 'idUsuario' | 'especialidads'> | null;
  medicamentos?: Pick<IMedicamento, 'id' | 'nombre' | 'dosis'>[] | null;
  informe?: Pick<IInforme, 'id'> | null | undefined;
}

export type NewReceta = Omit<IReceta, 'id'> & { id: null };
