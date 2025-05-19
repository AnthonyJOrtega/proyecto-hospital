import { IEspecialidad, NewEspecialidad } from './especialidad.model';

export const sampleWithRequiredData: IEspecialidad = {
  id: 10308,
};

export const sampleWithPartialData: IEspecialidad = {
  id: 14436,
  nombre: 'castanet',
  descripcion: 'however',
};

export const sampleWithFullData: IEspecialidad = {
  id: 13127,
  nombre: 'that break',
  descripcion: 'openly incidentally',
};

export const sampleWithNewData: NewEspecialidad = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
