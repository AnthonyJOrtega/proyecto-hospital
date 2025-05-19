import { IMedicamento, NewMedicamento } from './medicamento.model';

export const sampleWithRequiredData: IMedicamento = {
  id: 2102,
};

export const sampleWithPartialData: IMedicamento = {
  id: 8130,
  descripcion: 'lest',
};

export const sampleWithFullData: IMedicamento = {
  id: 26869,
  nombre: 'unnecessarily consequently lawmaker',
  descripcion: 'duh swine downright',
  dosis: 'weight where yum',
};

export const sampleWithNewData: NewMedicamento = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
