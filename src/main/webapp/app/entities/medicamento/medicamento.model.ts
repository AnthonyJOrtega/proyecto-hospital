import { IReceta } from 'app/entities/receta/receta.model';

export interface IMedicamento {
  id: number;
  nombre?: string | null;
  descripcion?: string | null;
  dosis?: string | null;
  recetas?: Pick<IReceta, 'id'>[] | null;
}

export type NewMedicamento = Omit<IMedicamento, 'id'> & { id: null };
