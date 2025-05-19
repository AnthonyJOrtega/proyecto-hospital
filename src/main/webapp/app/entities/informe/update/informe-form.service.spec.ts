import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../informe.test-samples';

import { InformeFormService } from './informe-form.service';

describe('Informe Form Service', () => {
  let service: InformeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InformeFormService);
  });

  describe('Service methods', () => {
    describe('createInformeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInformeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fecha: expect.any(Object),
            resumen: expect.any(Object),
            receta: expect.any(Object),
            paciente: expect.any(Object),
            trabajador: expect.any(Object),
            enfermedads: expect.any(Object),
          }),
        );
      });

      it('passing IInforme should create a new form with FormGroup', () => {
        const formGroup = service.createInformeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fecha: expect.any(Object),
            resumen: expect.any(Object),
            receta: expect.any(Object),
            paciente: expect.any(Object),
            trabajador: expect.any(Object),
            enfermedads: expect.any(Object),
          }),
        );
      });
    });

    describe('getInforme', () => {
      it('should return NewInforme for default Informe initial value', () => {
        const formGroup = service.createInformeFormGroup(sampleWithNewData);

        const informe = service.getInforme(formGroup) as any;

        expect(informe).toMatchObject(sampleWithNewData);
      });

      it('should return NewInforme for empty Informe initial value', () => {
        const formGroup = service.createInformeFormGroup();

        const informe = service.getInforme(formGroup) as any;

        expect(informe).toMatchObject({});
      });

      it('should return IInforme', () => {
        const formGroup = service.createInformeFormGroup(sampleWithRequiredData);

        const informe = service.getInforme(formGroup) as any;

        expect(informe).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInforme should not enable id FormControl', () => {
        const formGroup = service.createInformeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInforme should disable id FormControl', () => {
        const formGroup = service.createInformeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
