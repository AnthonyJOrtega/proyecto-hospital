import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../enfermedad.test-samples';

import { EnfermedadFormService } from './enfermedad-form.service';

describe('Enfermedad Form Service', () => {
  let service: EnfermedadFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EnfermedadFormService);
  });

  describe('Service methods', () => {
    describe('createEnfermedadFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEnfermedadFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            descripcion: expect.any(Object),
            informes: expect.any(Object),
          }),
        );
      });

      it('passing IEnfermedad should create a new form with FormGroup', () => {
        const formGroup = service.createEnfermedadFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            descripcion: expect.any(Object),
            informes: expect.any(Object),
          }),
        );
      });
    });

    describe('getEnfermedad', () => {
      it('should return NewEnfermedad for default Enfermedad initial value', () => {
        const formGroup = service.createEnfermedadFormGroup(sampleWithNewData);

        const enfermedad = service.getEnfermedad(formGroup) as any;

        expect(enfermedad).toMatchObject(sampleWithNewData);
      });

      it('should return NewEnfermedad for empty Enfermedad initial value', () => {
        const formGroup = service.createEnfermedadFormGroup();

        const enfermedad = service.getEnfermedad(formGroup) as any;

        expect(enfermedad).toMatchObject({});
      });

      it('should return IEnfermedad', () => {
        const formGroup = service.createEnfermedadFormGroup(sampleWithRequiredData);

        const enfermedad = service.getEnfermedad(formGroup) as any;

        expect(enfermedad).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEnfermedad should not enable id FormControl', () => {
        const formGroup = service.createEnfermedadFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEnfermedad should disable id FormControl', () => {
        const formGroup = service.createEnfermedadFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
