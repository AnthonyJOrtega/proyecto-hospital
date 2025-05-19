import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ITrabajador } from 'app/entities/trabajador/trabajador.model';
import { TrabajadorService } from 'app/entities/trabajador/service/trabajador.service';
import { IDireccion } from 'app/entities/direccion/direccion.model';
import { DireccionService } from 'app/entities/direccion/service/direccion.service';
import { IPaciente } from '../paciente.model';
import { PacienteService } from '../service/paciente.service';
import { PacienteFormService } from './paciente-form.service';

import { PacienteUpdateComponent } from './paciente-update.component';

describe('Paciente Management Update Component', () => {
  let comp: PacienteUpdateComponent;
  let fixture: ComponentFixture<PacienteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pacienteFormService: PacienteFormService;
  let pacienteService: PacienteService;
  let trabajadorService: TrabajadorService;
  let direccionService: DireccionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PacienteUpdateComponent],
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
      .overrideTemplate(PacienteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PacienteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pacienteFormService = TestBed.inject(PacienteFormService);
    pacienteService = TestBed.inject(PacienteService);
    trabajadorService = TestBed.inject(TrabajadorService);
    direccionService = TestBed.inject(DireccionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Trabajador query and add missing value', () => {
      const paciente: IPaciente = { id: 1591 };
      const trabajadors: ITrabajador[] = [{ id: 528 }];
      paciente.trabajadors = trabajadors;

      const trabajadorCollection: ITrabajador[] = [{ id: 528 }];
      jest.spyOn(trabajadorService, 'query').mockReturnValue(of(new HttpResponse({ body: trabajadorCollection })));
      const additionalTrabajadors = [...trabajadors];
      const expectedCollection: ITrabajador[] = [...additionalTrabajadors, ...trabajadorCollection];
      jest.spyOn(trabajadorService, 'addTrabajadorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paciente });
      comp.ngOnInit();

      expect(trabajadorService.query).toHaveBeenCalled();
      expect(trabajadorService.addTrabajadorToCollectionIfMissing).toHaveBeenCalledWith(
        trabajadorCollection,
        ...additionalTrabajadors.map(expect.objectContaining),
      );
      expect(comp.trabajadorsSharedCollection).toEqual(expectedCollection);
    });

    it('should call Direccion query and add missing value', () => {
      const paciente: IPaciente = { id: 1591 };
      const direccions: IDireccion[] = [{ id: 31929 }];
      paciente.direccions = direccions;

      const direccionCollection: IDireccion[] = [{ id: 31929 }];
      jest.spyOn(direccionService, 'query').mockReturnValue(of(new HttpResponse({ body: direccionCollection })));
      const additionalDireccions = [...direccions];
      const expectedCollection: IDireccion[] = [...additionalDireccions, ...direccionCollection];
      jest.spyOn(direccionService, 'addDireccionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paciente });
      comp.ngOnInit();

      expect(direccionService.query).toHaveBeenCalled();
      expect(direccionService.addDireccionToCollectionIfMissing).toHaveBeenCalledWith(
        direccionCollection,
        ...additionalDireccions.map(expect.objectContaining),
      );
      expect(comp.direccionsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const paciente: IPaciente = { id: 1591 };
      const trabajador: ITrabajador = { id: 528 };
      paciente.trabajadors = [trabajador];
      const direccion: IDireccion = { id: 31929 };
      paciente.direccions = [direccion];

      activatedRoute.data = of({ paciente });
      comp.ngOnInit();

      expect(comp.trabajadorsSharedCollection).toContainEqual(trabajador);
      expect(comp.direccionsSharedCollection).toContainEqual(direccion);
      expect(comp.paciente).toEqual(paciente);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaciente>>();
      const paciente = { id: 25847 };
      jest.spyOn(pacienteFormService, 'getPaciente').mockReturnValue(paciente);
      jest.spyOn(pacienteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paciente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paciente }));
      saveSubject.complete();

      // THEN
      expect(pacienteFormService.getPaciente).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pacienteService.update).toHaveBeenCalledWith(expect.objectContaining(paciente));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaciente>>();
      const paciente = { id: 25847 };
      jest.spyOn(pacienteFormService, 'getPaciente').mockReturnValue({ id: null });
      jest.spyOn(pacienteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paciente: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paciente }));
      saveSubject.complete();

      // THEN
      expect(pacienteFormService.getPaciente).toHaveBeenCalled();
      expect(pacienteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaciente>>();
      const paciente = { id: 25847 };
      jest.spyOn(pacienteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paciente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pacienteService.update).toHaveBeenCalled();
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
