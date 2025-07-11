import { IEspecialidad } from 'app/entities/especialidad/especialidad.model';
import { ICita } from 'app/entities/cita/cita.model';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { IDireccion } from 'app/entities/direccion/direccion.model';
import { Puesto } from 'app/entities/enumerations/puesto.model';
import { Turno } from 'app/entities/enumerations/turno.model';

export interface ITrabajador {
  id: number;
  idUsuario?: number | null;
  nombre?: string | null;
  apellido?: string | null;
  dni?: string | null;
  puesto?: keyof typeof Puesto | null;
  disponibilidad?: boolean | null;
  turno?: keyof typeof Turno | null;
  especialidads?: Pick<IEspecialidad, 'id' | 'nombre'>[] | null;
  citas?: Pick<ICita, 'id' | 'paciente'>[] | null;
  pacientes?: Pick<IPaciente, 'id' | 'nombre' | 'apellido'>[] | null;
  direccions?: Pick<IDireccion, 'id' | 'calle' | 'codigoPostal' | 'numero' | 'ciudad' | 'pais'>[] | null;
}

export type NewTrabajador = Omit<ITrabajador, 'id'> & { id: null };
