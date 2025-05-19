import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IInforme } from 'app/entities/informe/informe.model';
import { InformeService } from 'app/entities/informe/service/informe.service';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';
import { ITrabajador } from 'app/entities/trabajador/trabajador.model';
import { TrabajadorService } from 'app/entities/trabajador/service/trabajador.service';
import { ICita } from '../cita.model';
import { CitaService } from '../service/cita.service';
import { CitaFormService } from './cita-form.service';

import { CitaUpdateComponent } from './cita-update.component';

describe('Cita Management Update Component', () => {
  let comp: CitaUpdateComponent;
  let fixture: ComponentFixture<CitaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let citaFormService: CitaFormService;
  let citaService: CitaService;
  let informeService: InformeService;
  let pacienteService: PacienteService;
  let trabajadorService: TrabajadorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CitaUpdateComponent],
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
      .overrideTemplate(CitaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CitaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    citaFormService = TestBed.inject(CitaFormService);
    citaService = TestBed.inject(CitaService);
    informeService = TestBed.inject(InformeService);
    pacienteService = TestBed.inject(PacienteService);
    trabajadorService = TestBed.inject(TrabajadorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call informe query and add missing value', () => {
      const cita: ICita = { id: 12445 };
      const informe: IInforme = { id: 15736 };
      cita.informe = informe;

      const informeCollection: IInforme[] = [{ id: 15736 }];
      jest.spyOn(informeService, 'query').mockReturnValue(of(new HttpResponse({ body: informeCollection })));
      const expectedCollection: IInforme[] = [informe, ...informeCollection];
      jest.spyOn(informeService, 'addInformeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cita });
      comp.ngOnInit();

      expect(informeService.query).toHaveBeenCalled();
      expect(informeService.addInformeToCollectionIfMissing).toHaveBeenCalledWith(informeCollection, informe);
      expect(comp.informesCollection).toEqual(expectedCollection);
    });

    it('should call Paciente query and add missing value', () => {
      const cita: ICita = { id: 12445 };
      const paciente: IPaciente = { id: 25847 };
      cita.paciente = paciente;

      const pacienteCollection: IPaciente[] = [{ id: 25847 }];
      jest.spyOn(pacienteService, 'query').mockReturnValue(of(new HttpResponse({ body: pacienteCollection })));
      const additionalPacientes = [paciente];
      const expectedCollection: IPaciente[] = [...additionalPacientes, ...pacienteCollection];
      jest.spyOn(pacienteService, 'addPacienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cita });
      comp.ngOnInit();

      expect(pacienteService.query).toHaveBeenCalled();
      expect(pacienteService.addPacienteToCollectionIfMissing).toHaveBeenCalledWith(
        pacienteCollection,
        ...additionalPacientes.map(expect.objectContaining),
      );
      expect(comp.pacientesSharedCollection).toEqual(expectedCollection);
    });

    it('should call Trabajador query and add missing value', () => {
      const cita: ICita = { id: 12445 };
      const trabajadors: ITrabajador[] = [{ id: 528 }];
      cita.trabajadors = trabajadors;

      const trabajadorCollection: ITrabajador[] = [{ id: 528 }];
      jest.spyOn(trabajadorService, 'query').mockReturnValue(of(new HttpResponse({ body: trabajadorCollection })));
      const additionalTrabajadors = [...trabajadors];
      const expectedCollection: ITrabajador[] = [...additionalTrabajadors, ...trabajadorCollection];
      jest.spyOn(trabajadorService, 'addTrabajadorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cita });
      comp.ngOnInit();

      expect(trabajadorService.query).toHaveBeenCalled();
      expect(trabajadorService.addTrabajadorToCollectionIfMissing).toHaveBeenCalledWith(
        trabajadorCollection,
        ...additionalTrabajadors.map(expect.objectContaining),
      );
      expect(comp.trabajadorsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const cita: ICita = { id: 12445 };
      const informe: IInforme = { id: 15736 };
      cita.informe = informe;
      const paciente: IPaciente = { id: 25847 };
      cita.paciente = paciente;
      const trabajador: ITrabajador = { id: 528 };
      cita.trabajadors = [trabajador];

      activatedRoute.data = of({ cita });
      comp.ngOnInit();

      expect(comp.informesCollection).toContainEqual(informe);
      expect(comp.pacientesSharedCollection).toContainEqual(paciente);
      expect(comp.trabajadorsSharedCollection).toContainEqual(trabajador);
      expect(comp.cita).toEqual(cita);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICita>>();
      const cita = { id: 7379 };
      jest.spyOn(citaFormService, 'getCita').mockReturnValue(cita);
      jest.spyOn(citaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cita });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cita }));
      saveSubject.complete();

      // THEN
      expect(citaFormService.getCita).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(citaService.update).toHaveBeenCalledWith(expect.objectContaining(cita));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICita>>();
      const cita = { id: 7379 };
      jest.spyOn(citaFormService, 'getCita').mockReturnValue({ id: null });
      jest.spyOn(citaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cita: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cita }));
      saveSubject.complete();

      // THEN
      expect(citaFormService.getCita).toHaveBeenCalled();
      expect(citaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICita>>();
      const cita = { id: 7379 };
      jest.spyOn(citaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cita });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(citaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareInforme', () => {
      it('should forward to informeService', () => {
        const entity = { id: 15736 };
        const entity2 = { id: 29544 };
        jest.spyOn(informeService, 'compareInforme');
        comp.compareInforme(entity, entity2);
        expect(informeService.compareInforme).toHaveBeenCalledWith(entity, entity2);
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
