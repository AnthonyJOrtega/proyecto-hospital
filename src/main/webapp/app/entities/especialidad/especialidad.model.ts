import { ITrabajador } from 'app/entities/trabajador/trabajador.model';

export interface IEspecialidad {
  id: number;
  nombre?: string | null;
  descripcion?: string | null;
  trabajadors?: Pick<ITrabajador, 'id' | 'idUsuario'>[] | null;
}

export type NewEspecialidad = Omit<IEspecialidad, 'id'> & { id: null };
