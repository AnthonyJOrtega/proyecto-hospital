import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../receta.test-samples';

import { RecetaFormService } from './receta-form.service';

describe('Receta Form Service', () => {
  let service: RecetaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RecetaFormService);
  });

  describe('Service methods', () => {
    describe('createRecetaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRecetaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fechaInicio: expect.any(Object),
            fechaFin: expect.any(Object),
            instrucciones: expect.any(Object),
            paciente: expect.any(Object),
            trabajador: expect.any(Object),
            medicamentos: expect.any(Object),
          }),
        );
      });

      it('passing IReceta should create a new form with FormGroup', () => {
        const formGroup = service.createRecetaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fechaInicio: expect.any(Object),
            fechaFin: expect.any(Object),
            instrucciones: expect.any(Object),
            paciente: expect.any(Object),
            trabajador: expect.any(Object),
            medicamentos: expect.any(Object),
          }),
        );
      });
    });

    describe('getReceta', () => {
      it('should return NewReceta for default Receta initial value', () => {
        const formGroup = service.createRecetaFormGroup(sampleWithNewData);

        const receta = service.getReceta(formGroup) as any;

        expect(receta).toMatchObject(sampleWithNewData);
      });

      it('should return NewReceta for empty Receta initial value', () => {
        const formGroup = service.createRecetaFormGroup();

        const receta = service.getReceta(formGroup) as any;

        expect(receta).toMatchObject({});
      });

      it('should return IReceta', () => {
        const formGroup = service.createRecetaFormGroup(sampleWithRequiredData);

        const receta = service.getReceta(formGroup) as any;

        expect(receta).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IReceta should not enable id FormControl', () => {
        const formGroup = service.createRecetaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewReceta should disable id FormControl', () => {
        const formGroup = service.createRecetaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
