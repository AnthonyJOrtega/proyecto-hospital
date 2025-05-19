import { IInforme, NewInforme } from './informe.model';

export const sampleWithRequiredData: IInforme = {
  id: 101,
};

export const sampleWithPartialData: IInforme = {
  id: 26443,
};

export const sampleWithFullData: IInforme = {
  id: 15405,
  fecha: 'unless handsome',
  resumen: 'which',
};

export const sampleWithNewData: NewInforme = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
