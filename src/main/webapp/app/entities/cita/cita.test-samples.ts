import dayjs from 'dayjs/esm';

import { ICita, NewCita } from './cita.model';

export const sampleWithRequiredData: ICita = {
  id: 25348,
};

export const sampleWithPartialData: ICita = {
  id: 19583,
  estadoCita: 'FINALIZADO',
};

export const sampleWithFullData: ICita = {
  id: 26412,
  fechaCreacion: dayjs('2025-05-19'),
  estadoCita: 'FINALIZADO',
  observaciones: 'unlawful rightfully',
};

export const sampleWithNewData: NewCita = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
