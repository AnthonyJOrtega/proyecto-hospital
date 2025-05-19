import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IEspecialidad } from 'app/entities/especialidad/especialidad.model';
import { EspecialidadService } from 'app/entities/especialidad/service/especialidad.service';
import { ICita } from 'app/entities/cita/cita.model';
import { CitaService } from 'app/entities/cita/service/cita.service';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';
import { IDireccion } from 'app/entities/direccion/direccion.model';
import { DireccionService } from 'app/entities/direccion/service/direccion.service';
import { ITrabajador } from '../trabajador.model';
import { TrabajadorService } from '../service/trabajador.service';
import { TrabajadorFormService } from './trabajador-form.service';

import { TrabajadorUpdateComponent } from './trabajador-update.component';

describe('Trabajador Management Update Component', () => {
  let comp: TrabajadorUpdateComponent;
  let fixture: ComponentFixture<TrabajadorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let trabajadorFormService: TrabajadorFormService;
  let trabajadorService: TrabajadorService;
  let especialidadService: EspecialidadService;
  let citaService: CitaService;
  let pacienteService: PacienteService;
  let direccionService: DireccionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TrabajadorUpdateComponent],
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
      .overrideTemplate(TrabajadorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TrabajadorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    trabajadorFormService = TestBed.inject(TrabajadorFormService);
    trabajadorService = TestBed.inject(TrabajadorService);
    especialidadService = TestBed.inject(EspecialidadService);
    citaService = TestBed.inject(CitaService);
    pacienteService = TestBed.inject(PacienteService);
    direccionService = TestBed.inject(DireccionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Especialidad query and add missing value', () => {
      const trabajador: ITrabajador = { id: 4393 };
      const especialidads: IEspecialidad[] = [{ id: 9300 }];
      trabajador.especialidads = especialidads;

      const especialidadCollection: IEspecialidad[] = [{ id: 9300 }];
      jest.spyOn(especialidadService, 'query').mockReturnValue(of(new HttpResponse({ body: especialidadCollection })));
      const additionalEspecialidads = [...especialidads];
      const expectedCollection: IEspecialidad[] = [...additionalEspecialidads, ...especialidadCollection];
      jest.spyOn(especialidadService, 'addEspecialidadToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ trabajador });
      comp.ngOnInit();

      expect(especialidadService.query).toHaveBeenCalled();
      expect(especialidadService.addEspecialidadToCollectionIfMissing).toHaveBeenCalledWith(
        especialidadCollection,
        ...additionalEspecialidads.map(expect.objectContaining),
      );
      expect(comp.especialidadsSharedCollection).toEqual(expectedCollection);
    });

    it('should call Cita query and add missing value', () => {
      const trabajador: ITrabajador = { id: 4393 };
      const citas: ICita[] = [{ id: 7379 }];
      trabajador.citas = citas;

      const citaCollection: ICita[] = [{ id: 7379 }];
      jest.spyOn(citaService, 'query').mockReturnValue(of(new HttpResponse({ body: citaCollection })));
      const additionalCitas = [...citas];
      const expectedCollection: ICita[] = [...additionalCitas, ...citaCollection];
      jest.spyOn(citaService, 'addCitaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ trabajador });
      comp.ngOnInit();

      expect(citaService.query).toHaveBeenCalled();
      expect(citaService.addCitaToCollectionIfMissing).toHaveBeenCalledWith(
        citaCollection,
        ...additionalCitas.map(expect.objectContaining),
      );
      expect(comp.citasSharedCollection).toEqual(expectedCollection);
    });

    it('should call Paciente query and add missing value', () => {
      const trabajador: ITrabajador = { id: 4393 };
      const pacientes: IPaciente[] = [{ id: 25847 }];
      trabajador.pacientes = pacientes;

      const pacienteCollection: IPaciente[] = [{ id: 25847 }];
      jest.spyOn(pacienteService, 'query').mockReturnValue(of(new HttpResponse({ body: pacienteCollection })));
      const additionalPacientes = [...pacientes];
      const expectedCollection: IPaciente[] = [...additionalPacientes, ...pacienteCollection];
      jest.spyOn(pacienteService, 'addPacienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ trabajador });
      comp.ngOnInit();

      expect(pacienteService.query).toHaveBeenCalled();
      expect(pacienteService.addPacienteToCollectionIfMissing).toHaveBeenCalledWith(
        pacienteCollection,
        ...additionalPacientes.map(expect.objectContaining),
      );
      expect(comp.pacientesSharedCollection).toEqual(expectedCollection);
    });

    it('should call Direccion query and add missing value', () => {
      const trabajador: ITrabajador = { id: 4393 };
      const direccions: IDireccion[] = [{ id: 31929 }];
      trabajador.direccions = direccions;

      const direccionCollection: IDireccion[] = [{ id: 31929 }];
      jest.spyOn(direccionService, 'query').mockReturnValue(of(new HttpResponse({ body: direccionCollection })));
      const additionalDireccions = [...direccions];
      const expectedCollection: IDireccion[] = [...additionalDireccions, ...direccionCollection];
      jest.spyOn(direccionService, 'addDireccionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ trabajador });
      comp.ngOnInit();

      expect(direccionService.query).toHaveBeenCalled();
      expect(direccionService.addDireccionToCollectionIfMissing).toHaveBeenCalledWith(
        direccionCollection,
        ...additionalDireccions.map(expect.objectContaining),
      );
      expect(comp.direccionsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const trabajador: ITrabajador = { id: 4393 };
      const especialidad: IEspecialidad = { id: 9300 };
      trabajador.especialidads = [especialidad];
      const cita: ICita = { id: 7379 };
      trabajador.citas = [cita];
      const paciente: IPaciente = { id: 25847 };
      trabajador.pacientes = [paciente];
      const direccion: IDireccion = { id: 31929 };
      trabajador.direccions = [direccion];

      activatedRoute.data = of({ trabajador });
      comp.ngOnInit();

      expect(comp.especialidadsSharedCollection).toContainEqual(especialidad);
      expect(comp.citasSharedCollection).toContainEqual(cita);
      expect(comp.pacientesSharedCollection).toContainEqual(paciente);
      expect(comp.direccionsSharedCollection).toContainEqual(direccion);
      expect(comp.trabajador).toEqual(trabajador);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrabajador>>();
      const trabajador = { id: 528 };
      jest.spyOn(trabajadorFormService, 'getTrabajador').mockReturnValue(trabajador);
      jest.spyOn(trabajadorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trabajador });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trabajador }));
      saveSubject.complete();

      // THEN
      expect(trabajadorFormService.getTrabajador).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(trabajadorService.update).toHaveBeenCalledWith(expect.objectContaining(trabajador));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrabajador>>();
      const trabajador = { id: 528 };
      jest.spyOn(trabajadorFormService, 'getTrabajador').mockReturnValue({ id: null });
      jest.spyOn(trabajadorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trabajador: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trabajador }));
      saveSubject.complete();

      // THEN
      expect(trabajadorFormService.getTrabajador).toHaveBeenCalled();
      expect(trabajadorService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrabajador>>();
      const trabajador = { id: 528 };
      jest.spyOn(trabajadorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trabajador });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(trabajadorService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEspecialidad', () => {
      it('should forward to especialidadService', () => {
        const entity = { id: 9300 };
        const entity2 = { id: 20529 };
        jest.spyOn(especialidadService, 'compareEspecialidad');
        comp.compareEspecialidad(entity, entity2);
        expect(especialidadService.compareEspecialidad).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCita', () => {
      it('should forward to citaService', () => {
        const entity = { id: 7379 };
        const entity2 = { id: 12445 };
        jest.spyOn(citaService, 'compareCita');
        comp.compareCita(entity, entity2);
        expect(citaService.compareCita).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePaciente', () => {
      it('should forward to pacienteService', () => {
        const entity = { id: 25847 };
        const entity2 = { id: 1591 };
        jest.spyOn(pacienteService, 'comparePaciente');
        comp.comparePaciente(entity, entity2);
        expect(pacienteService.comparePaciente).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDireccion', () => {
      it('should forward to direccionService', () => {
        const entity = { id: 31929 };
        const entity2 = { id: 766 };
        jest.spyOn(direccionService, 'compareDireccion');
        comp.compareDireccion(entity, entity2);
        expect(direccionService.compareDireccion).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
