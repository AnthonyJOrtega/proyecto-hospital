import { IDireccion, NewDireccion } from './direccion.model';

export const sampleWithRequiredData: IDireccion = {
  id: 9445,
};

export const sampleWithPartialData: IDireccion = {
  id: 20529,
  pais: 'brook',
  ciudad: 'however ew fellow',
  localidad: 'why tenderly',
  codigoPostal: 30726,
  numero: 'brr manipulate marksman',
};

export const sampleWithFullData: IDireccion = {
  id: 12937,
  pais: 'upon yowza',
  ciudad: 'brand gah supposing',
  localidad: 'rim typeface dismal',
  codigoPostal: 9800,
  calle: 'rejigger queasily plain',
  numero: 'cop-out',
};

export const sampleWithNewData: NewDireccion = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
