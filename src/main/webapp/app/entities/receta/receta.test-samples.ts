import dayjs from 'dayjs/esm';

import { IReceta, NewReceta } from './receta.model';

export const sampleWithRequiredData: IReceta = {
  id: 24037,
};

export const sampleWithPartialData: IReceta = {
  id: 32106,
  instrucciones: 'slowly yowza official',
};

export const sampleWithFullData: IReceta = {
  id: 19221,
  fechaInicio: dayjs('2025-05-19'),
  fechaFin: dayjs('2025-05-19'),
  instrucciones: 'zowie meanwhile or',
};

export const sampleWithNewData: NewReceta = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
