import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ITrabajador } from 'app/entities/trabajador/trabajador.model';
import { TrabajadorService } from 'app/entities/trabajador/service/trabajador.service';
import { EspecialidadService } from '../service/especialidad.service';
import { IEspecialidad } from '../especialidad.model';
import { EspecialidadFormService } from './especialidad-form.service';

import { EspecialidadUpdateComponent } from './especialidad-update.component';

describe('Especialidad Management Update Component', () => {
  let comp: EspecialidadUpdateComponent;
  let fixture: ComponentFixture<EspecialidadUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let especialidadFormService: EspecialidadFormService;
  let especialidadService: EspecialidadService;
  let trabajadorService: TrabajadorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EspecialidadUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(EspecialidadUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EspecialidadUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    especialidadFormService = TestBed.inject(EspecialidadFormService);
    especialidadService = TestBed.inject(EspecialidadService);
    trabajadorService = TestBed.inject(TrabajadorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Trabajador query and add missing value', () => {
      const especialidad: IEspecialidad = { id: 20529 };
      const trabajadors: ITrabajador[] = [{ id: 528 }];
      especialidad.trabajadors = trabajadors;

      const trabajadorCollection: ITrabajador[] = [{ id: 528 }];
      jest.spyOn(trabajadorService, 'query').mockReturnValue(of(new HttpResponse({ body: trabajadorCollection })));
      const additionalTrabajadors = [...trabajadors];
      const expectedCollection: ITrabajador[] = [...additionalTrabajadors, ...trabajadorCollection];
      jest.spyOn(trabajadorService, 'addTrabajadorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ especialidad });
      comp.ngOnInit();

      expect(trabajadorService.query).toHaveBeenCalled();
      expect(trabajadorService.addTrabajadorToCollectionIfMissing).toHaveBeenCalledWith(
        trabajadorCollection,
        ...additionalTrabajadors.map(expect.objectContaining),
      );
      expect(comp.trabajadorsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const especialidad: IEspecialidad = { id: 20529 };
      const trabajador: ITrabajador = { id: 528 };
      especialidad.trabajadors = [trabajador];

      activatedRoute.data = of({ especialidad });
      comp.ngOnInit();

      expect(comp.trabajadorsSharedCollection).toContainEqual(trabajador);
      expect(comp.especialidad).toEqual(especialidad);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEspecialidad>>();
      const especialidad = { id: 9300 };
      jest.spyOn(especialidadFormService, 'getEspecialidad').mockReturnValue(especialidad);
      jest.spyOn(especialidadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ especialidad });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: especialidad }));
      saveSubject.complete();

      // THEN
      expect(especialidadFormService.getEspecialidad).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(especialidadService.update).toHaveBeenCalledWith(expect.objectContaining(especialidad));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEspecialidad>>();
      const especialidad = { id: 9300 };
      jest.spyOn(especialidadFormService, 'getEspecialidad').mockReturnValue({ id: null });
      jest.spyOn(especialidadService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ especialidad: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: especialidad }));
      saveSubject.complete();

      // THEN
      expect(especialidadFormService.getEspecialidad).toHaveBeenCalled();
      expect(especialidadService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEspecialidad>>();
      const especialidad = { id: 9300 };
      jest.spyOn(especialidadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ especialidad });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(especialidadService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTrabajador', () => {
      it('should forward to trabajadorService', () => {
        const entity = { id: 528 };
        const entity2 = { id: 4393 };
        jest.spyOn(trabajadorService, 'compareTrabajador');
        comp.compareTrabajador(entity, entity2);
        expect(trabajadorService.compareTrabajador).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
