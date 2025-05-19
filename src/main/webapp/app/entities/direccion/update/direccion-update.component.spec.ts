import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';
import { ITrabajador } from 'app/entities/trabajador/trabajador.model';
import { TrabajadorService } from 'app/entities/trabajador/service/trabajador.service';
import { IDireccion } from '../direccion.model';
import { DireccionService } from '../service/direccion.service';
import { DireccionFormService } from './direccion-form.service';

import { DireccionUpdateComponent } from './direccion-update.component';

describe('Direccion Management Update Component', () => {
  let comp: DireccionUpdateComponent;
  let fixture: ComponentFixture<DireccionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let direccionFormService: DireccionFormService;
  let direccionService: DireccionService;
  let pacienteService: PacienteService;
  let trabajadorService: TrabajadorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DireccionUpdateComponent],
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
      .overrideTemplate(DireccionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DireccionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    direccionFormService = TestBed.inject(DireccionFormService);
    direccionService = TestBed.inject(DireccionService);
    pacienteService = TestBed.inject(PacienteService);
    trabajadorService = TestBed.inject(TrabajadorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Paciente query and add missing value', () => {
      const direccion: IDireccion = { id: 766 };
      const pacientes: IPaciente[] = [{ id: 25847 }];
      direccion.pacientes = pacientes;

      const pacienteCollection: IPaciente[] = [{ id: 25847 }];
      jest.spyOn(pacienteService, 'query').mockReturnValue(of(new HttpResponse({ body: pacienteCollection })));
      const additionalPacientes = [...pacientes];
      const expectedCollection: IPaciente[] = [...additionalPacientes, ...pacienteCollection];
      jest.spyOn(pacienteService, 'addPacienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ direccion });
      comp.ngOnInit();

      expect(pacienteService.query).toHaveBeenCalled();
      expect(pacienteService.addPacienteToCollectionIfMissing).toHaveBeenCalledWith(
        pacienteCollection,
        ...additionalPacientes.map(expect.objectContaining),
      );
      expect(comp.pacientesSharedCollection).toEqual(expectedCollection);
    });

    it('should call Trabajador query and add missing value', () => {
      const direccion: IDireccion = { id: 766 };
      const trabajadors: ITrabajador[] = [{ id: 528 }];
      direccion.trabajadors = trabajadors;

      const trabajadorCollection: ITrabajador[] = [{ id: 528 }];
      jest.spyOn(trabajadorService, 'query').mockReturnValue(of(new HttpResponse({ body: trabajadorCollection })));
      const additionalTrabajadors = [...trabajadors];
      const expectedCollection: ITrabajador[] = [...additionalTrabajadors, ...trabajadorCollection];
      jest.spyOn(trabajadorService, 'addTrabajadorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ direccion });
      comp.ngOnInit();

      expect(trabajadorService.query).toHaveBeenCalled();
      expect(trabajadorService.addTrabajadorToCollectionIfMissing).toHaveBeenCalledWith(
        trabajadorCollection,
        ...additionalTrabajadors.map(expect.objectContaining),
      );
      expect(comp.trabajadorsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const direccion: IDireccion = { id: 766 };
      const paciente: IPaciente = { id: 25847 };
      direccion.pacientes = [paciente];
      const trabajador: ITrabajador = { id: 528 };
      direccion.trabajadors = [trabajador];

      activatedRoute.data = of({ direccion });
      comp.ngOnInit();

      expect(comp.pacientesSharedCollection).toContainEqual(paciente);
      expect(comp.trabajadorsSharedCollection).toContainEqual(trabajador);
      expect(comp.direccion).toEqual(direccion);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDireccion>>();
      const direccion = { id: 31929 };
      jest.spyOn(direccionFormService, 'getDireccion').mockReturnValue(direccion);
      jest.spyOn(direccionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ direccion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: direccion }));
      saveSubject.complete();

      // THEN
      expect(direccionFormService.getDireccion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(direccionService.update).toHaveBeenCalledWith(expect.objectContaining(direccion));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDireccion>>();
      const direccion = { id: 31929 };
      jest.spyOn(direccionFormService, 'getDireccion').mockReturnValue({ id: null });
      jest.spyOn(direccionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ direccion: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: direccion }));
      saveSubject.complete();

      // THEN
      expect(direccionFormService.getDireccion).toHaveBeenCalled();
      expect(direccionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDireccion>>();
      const direccion = { id: 31929 };
      jest.spyOn(direccionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ direccion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(direccionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePaciente', () => {
      it('should forward to pacienteService', () => {
        const entity = { id: 25847 };
        const entity2 = { id: 1591 };
        jest.spyOn(pacienteService, 'comparePaciente');
        comp.comparePaciente(entity, entity2);
        expect(pacienteService.comparePaciente).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
