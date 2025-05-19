import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../trabajador.test-samples';

import { TrabajadorFormService } from './trabajador-form.service';

describe('Trabajador Form Service', () => {
  let service: TrabajadorFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TrabajadorFormService);
  });

  describe('Service methods', () => {
    describe('createTrabajadorFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTrabajadorFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idUsuario: expect.any(Object),
            nombre: expect.any(Object),
            apellido: expect.any(Object),
            dni: expect.any(Object),
            puesto: expect.any(Object),
            disponibilidad: expect.any(Object),
            turno: expect.any(Object),
            especialidads: expect.any(Object),
            citas: expect.any(Object),
            pacientes: expect.any(Object),
            direccions: expect.any(Object),
          }),
        );
      });

      it('passing ITrabajador should create a new form with FormGroup', () => {
        const formGroup = service.createTrabajadorFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idUsuario: expect.any(Object),
            nombre: expect.any(Object),
            apellido: expect.any(Object),
            dni: expect.any(Object),
            puesto: expect.any(Object),
            disponibilidad: expect.any(Object),
            turno: expect.any(Object),
            especialidads: expect.any(Object),
            citas: expect.any(Object),
            pacientes: expect.any(Object),
            direccions: expect.any(Object),
          }),
        );
      });
    });

    describe('getTrabajador', () => {
      it('should return NewTrabajador for default Trabajador initial value', () => {
        const formGroup = service.createTrabajadorFormGroup(sampleWithNewData);

        const trabajador = service.getTrabajador(formGroup) as any;

        expect(trabajador).toMatchObject(sampleWithNewData);
      });

      it('should return NewTrabajador for empty Trabajador initial value', () => {
        const formGroup = service.createTrabajadorFormGroup();

        const trabajador = service.getTrabajador(formGroup) as any;

        expect(trabajador).toMatchObject({});
      });

      it('should return ITrabajador', () => {
        const formGroup = service.createTrabajadorFormGroup(sampleWithRequiredData);

        const trabajador = service.getTrabajador(formGroup) as any;

        expect(trabajador).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITrabajador should not enable id FormControl', () => {
        const formGroup = service.createTrabajadorFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTrabajador should disable id FormControl', () => {
        const formGroup = service.createTrabajadorFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
