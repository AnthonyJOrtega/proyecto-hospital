import dayjs from 'dayjs/esm';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { ITrabajador } from 'app/entities/trabajador/trabajador.model';
import { IMedicamento } from 'app/entities/medicamento/medicamento.model';

export interface IReceta {
  id: number;
  fechaInicio?: dayjs.Dayjs | null;
  fechaFin?: dayjs.Dayjs | null;
  instrucciones?: string | null;
  paciente?: Pick<IPaciente, 'id'> | null;
  trabajador?: Pick<ITrabajador, 'id'> | null;
  medicamentos?: Pick<IMedicamento, 'id'>[] | null;
}

export type NewReceta = Omit<IReceta, 'id'> & { id: null };
