import dayjs from 'dayjs/esm';

import { IPaciente, NewPaciente } from './paciente.model';

export const sampleWithRequiredData: IPaciente = {
  id: 25438,
};

export const sampleWithPartialData: IPaciente = {
  id: 22810,
  fechaNacimiento: dayjs('2025-05-19'),
};

export const sampleWithFullData: IPaciente = {
  id: 160,
  nombre: 'blind indeed truthfully',
  apellido: 'where louse who',
  dni: 'zowie searchingly naturally',
  seguroMedico: 'phew pastel cautiously',
  fechaNacimiento: dayjs('2025-05-18'),
  telefono: 'yum possible searchingly',
};

export const sampleWithNewData: NewPaciente = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
