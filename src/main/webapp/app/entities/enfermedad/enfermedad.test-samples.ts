import { IEnfermedad, NewEnfermedad } from './enfermedad.model';

export const sampleWithRequiredData: IEnfermedad = {
  id: 118,
};

export const sampleWithPartialData: IEnfermedad = {
  id: 28727,
  nombre: 'hmph',
  descripcion: 'excluding fat supposing',
};

export const sampleWithFullData: IEnfermedad = {
  id: 1741,
  nombre: 'reproachfully huzzah',
  descripcion: 'complication beneficial oxygenate',
};

export const sampleWithNewData: NewEnfermedad = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
