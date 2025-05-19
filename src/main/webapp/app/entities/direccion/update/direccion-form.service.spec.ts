import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../direccion.test-samples';

import { DireccionFormService } from './direccion-form.service';

describe('Direccion Form Service', () => {
  let service: DireccionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DireccionFormService);
  });

  describe('Service methods', () => {
    describe('createDireccionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDireccionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            pais: expect.any(Object),
            ciudad: expect.any(Object),
            localidad: expect.any(Object),
            codigoPostal: expect.any(Object),
            calle: expect.any(Object),
            numero: expect.any(Object),
            pacientes: expect.any(Object),
            trabajadors: expect.any(Object),
          }),
        );
      });

      it('passing IDireccion should create a new form with FormGroup', () => {
        const formGroup = service.createDireccionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            pais: expect.any(Object),
            ciudad: expect.any(Object),
            localidad: expect.any(Object),
            codigoPostal: expect.any(Object),
            calle: expect.any(Object),
            numero: expect.any(Object),
            pacientes: expect.any(Object),
            trabajadors: expect.any(Object),
          }),
        );
      });
    });

    describe('getDireccion', () => {
      it('should return NewDireccion for default Direccion initial value', () => {
        const formGroup = service.createDireccionFormGroup(sampleWithNewData);

        const direccion = service.getDireccion(formGroup) as any;

        expect(direccion).toMatchObject(sampleWithNewData);
      });

      it('should return NewDireccion for empty Direccion initial value', () => {
        const formGroup = service.createDireccionFormGroup();

        const direccion = service.getDireccion(formGroup) as any;

        expect(direccion).toMatchObject({});
      });

      it('should return IDireccion', () => {
        const formGroup = service.createDireccionFormGroup(sampleWithRequiredData);

        const direccion = service.getDireccion(formGroup) as any;

        expect(direccion).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDireccion should not enable id FormControl', () => {
        const formGroup = service.createDireccionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDireccion should disable id FormControl', () => {
        const formGroup = service.createDireccionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
