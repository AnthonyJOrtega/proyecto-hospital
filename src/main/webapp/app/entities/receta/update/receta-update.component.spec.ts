import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';
import { ITrabajador } from 'app/entities/trabajador/trabajador.model';
import { TrabajadorService } from 'app/entities/trabajador/service/trabajador.service';
import { IMedicamento } from 'app/entities/medicamento/medicamento.model';
import { MedicamentoService } from 'app/entities/medicamento/service/medicamento.service';
import { IReceta } from '../receta.model';
import { RecetaService } from '../service/receta.service';
import { RecetaFormService } from './receta-form.service';

import { RecetaUpdateComponent } from './receta-update.component';

describe('Receta Management Update Component', () => {
  let comp: RecetaUpdateComponent;
  let fixture: ComponentFixture<RecetaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let recetaFormService: RecetaFormService;
  let recetaService: RecetaService;
  let pacienteService: PacienteService;
  let trabajadorService: TrabajadorService;
  let medicamentoService: MedicamentoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RecetaUpdateComponent],
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
      .overrideTemplate(RecetaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RecetaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    recetaFormService = TestBed.inject(RecetaFormService);
    recetaService = TestBed.inject(RecetaService);
    pacienteService = TestBed.inject(PacienteService);
    trabajadorService = TestBed.inject(TrabajadorService);
    medicamentoService = TestBed.inject(MedicamentoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Paciente query and add missing value', () => {
      const receta: IReceta = { id: 15285 };
      const paciente: IPaciente = { id: 25847 };
      receta.paciente = paciente;

      const pacienteCollection: IPaciente[] = [{ id: 25847 }];
      jest.spyOn(pacienteService, 'query').mockReturnValue(of(new HttpResponse({ body: pacienteCollection })));
      const additionalPacientes = [paciente];
      const expectedCollection: IPaciente[] = [...additionalPacientes, ...pacienteCollection];
      jest.spyOn(pacienteService, 'addPacienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ receta });
      comp.ngOnInit();

      expect(pacienteService.query).toHaveBeenCalled();
      expect(pacienteService.addPacienteToCollectionIfMissing).toHaveBeenCalledWith(
        pacienteCollection,
        ...additionalPacientes.map(expect.objectContaining),
      );
      expect(comp.pacientesSharedCollection).toEqual(expectedCollection);
    });

    it('should call Trabajador query and add missing value', () => {
      const receta: IReceta = { id: 15285 };
      const trabajador: ITrabajador = { id: 528 };
      receta.trabajador = trabajador;

      const trabajadorCollection: ITrabajador[] = [{ id: 528 }];
      jest.spyOn(trabajadorService, 'query').mockReturnValue(of(new HttpResponse({ body: trabajadorCollection })));
      const additionalTrabajadors = [trabajador];
      const expectedCollection: ITrabajador[] = [...additionalTrabajadors, ...trabajadorCollection];
      jest.spyOn(trabajadorService, 'addTrabajadorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ receta });
      comp.ngOnInit();

      expect(trabajadorService.query).toHaveBeenCalled();
      expect(trabajadorService.addTrabajadorToCollectionIfMissing).toHaveBeenCalledWith(
        trabajadorCollection,
        ...additionalTrabajadors.map(expect.objectContaining),
      );
      expect(comp.trabajadorsSharedCollection).toEqual(expectedCollection);
    });

    it('should call Medicamento query and add missing value', () => {
      const receta: IReceta = { id: 15285 };
      const medicamentos: IMedicamento[] = [{ id: 6895 }];
      receta.medicamentos = medicamentos;

      const medicamentoCollection: IMedicamento[] = [{ id: 6895 }];
      jest.spyOn(medicamentoService, 'query').mockReturnValue(of(new HttpResponse({ body: medicamentoCollection })));
      const additionalMedicamentos = [...medicamentos];
      const expectedCollection: IMedicamento[] = [...additionalMedicamentos, ...medicamentoCollection];
      jest.spyOn(medicamentoService, 'addMedicamentoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ receta });
      comp.ngOnInit();

      expect(medicamentoService.query).toHaveBeenCalled();
      expect(medicamentoService.addMedicamentoToCollectionIfMissing).toHaveBeenCalledWith(
        medicamentoCollection,
        ...additionalMedicamentos.map(expect.objectContaining),
      );
      expect(comp.medicamentosSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const receta: IReceta = { id: 15285 };
      const paciente: IPaciente = { id: 25847 };
      receta.paciente = paciente;
      const trabajador: ITrabajador = { id: 528 };
      receta.trabajador = trabajador;
      const medicamento: IMedicamento = { id: 6895 };
      receta.medicamentos = [medicamento];

      activatedRoute.data = of({ receta });
      comp.ngOnInit();

      expect(comp.pacientesSharedCollection).toContainEqual(paciente);
      expect(comp.trabajadorsSharedCollection).toContainEqual(trabajador);
      expect(comp.medicamentosSharedCollection).toContainEqual(medicamento);
      expect(comp.receta).toEqual(receta);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReceta>>();
      const receta = { id: 15537 };
      jest.spyOn(recetaFormService, 'getReceta').mockReturnValue(receta);
      jest.spyOn(recetaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ receta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: receta }));
      saveSubject.complete();

      // THEN
      expect(recetaFormService.getReceta).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(recetaService.update).toHaveBeenCalledWith(expect.objectContaining(receta));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReceta>>();
      const receta = { id: 15537 };
      jest.spyOn(recetaFormService, 'getReceta').mockReturnValue({ id: null });
      jest.spyOn(recetaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ receta: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: receta }));
      saveSubject.complete();

      // THEN
      expect(recetaFormService.getReceta).toHaveBeenCalled();
      expect(recetaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReceta>>();
      const receta = { id: 15537 };
      jest.spyOn(recetaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ receta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(recetaService.update).toHaveBeenCalled();
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

    describe('compareMedicamento', () => {
      it('should forward to medicamentoService', () => {
        const entity = { id: 6895 };
        const entity2 = { id: 10825 };
        jest.spyOn(medicamentoService, 'compareMedicamento');
        comp.compareMedicamento(entity, entity2);
        expect(medicamentoService.compareMedicamento).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
