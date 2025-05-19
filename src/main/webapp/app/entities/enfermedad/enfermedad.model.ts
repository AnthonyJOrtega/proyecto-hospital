import { IInforme } from 'app/entities/informe/informe.model';

export interface IEnfermedad {
  id: number;
  nombre?: string | null;
  descripcion?: string | null;
  informes?: Pick<IInforme, 'id'>[] | null;
}

export type NewEnfermedad = Omit<IEnfermedad, 'id'> & { id: null };
