import { ITrabajador, NewTrabajador } from './trabajador.model';

export const sampleWithRequiredData: ITrabajador = {
  id: 24443,
};

export const sampleWithPartialData: ITrabajador = {
  id: 32222,
  idUsuario: 32472,
  nombre: 'tensely',
  apellido: 'noxious fuss against',
  puesto: 'CELADOR',
  turno: 'GUARDIA',
};

export const sampleWithFullData: ITrabajador = {
  id: 6676,
  idUsuario: 10677,
  nombre: 'yearly once whereas',
  apellido: 'in over',
  dni: 'onto',
  puesto: 'CELADOR',
  disponibilidad: false,
  turno: 'GUARDIA',
};

export const sampleWithNewData: NewTrabajador = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
