import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IReceta } from 'app/entities/receta/receta.model';
import { RecetaService } from 'app/entities/receta/service/receta.service';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';
import { ITrabajador } from 'app/entities/trabajador/trabajador.model';
import { TrabajadorService } from 'app/entities/trabajador/service/trabajador.service';
import { IEnfermedad } from 'app/entities/enfermedad/enfermedad.model';
import { EnfermedadService } from 'app/entities/enfermedad/service/enfermedad.service';
import { IInforme } from '../informe.model';
import { InformeService } from '../service/informe.service';
import { InformeFormService } from './informe-form.service';

import { InformeUpdateComponent } from './informe-update.component';

describe('Informe Management Update Component', () => {
  let comp: InformeUpdateComponent;
  let fixture: ComponentFixture<InformeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let informeFormService: InformeFormService;
  let informeService: InformeService;
  let recetaService: RecetaService;
  let pacienteService: PacienteService;
  let trabajadorService: TrabajadorService;
  let enfermedadService: EnfermedadService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [InformeUpdateComponent],
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
      .overrideTemplate(InformeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InformeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    informeFormService = TestBed.inject(InformeFormService);
    informeService = TestBed.inject(InformeService);
    recetaService = TestBed.inject(RecetaService);
    pacienteService = TestBed.inject(PacienteService);
    trabajadorService = TestBed.inject(TrabajadorService);
    enfermedadService = TestBed.inject(EnfermedadService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call receta query and add missing value', () => {
      const informe: IInforme = { id: 29544 };
      const receta: IReceta = { id: 15537 };
      informe.receta = receta;

      const recetaCollection: IReceta[] = [{ id: 15537 }];
      jest.spyOn(recetaService, 'query').mockReturnValue(of(new HttpResponse({ body: recetaCollection })));
      const expectedCollection: IReceta[] = [receta, ...recetaCollection];
      jest.spyOn(recetaService, 'addRecetaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ informe });
      comp.ngOnInit();

      expect(recetaService.query).toHaveBeenCalled();
      expect(recetaService.addRecetaToCollectionIfMissing).toHaveBeenCalledWith(recetaCollection, receta);
      expect(comp.recetasCollection).toEqual(expectedCollection);
    });

    it('should call Paciente query and add missing value', () => {
      const informe: IInforme = { id: 29544 };
      const paciente: IPaciente = { id: 25847 };
      informe.paciente = paciente;

      const pacienteCollection: IPaciente[] = [{ id: 25847 }];
      jest.spyOn(pacienteService, 'query').mockReturnValue(of(new HttpResponse({ body: pacienteCollection })));
      const additionalPacientes = [paciente];
      const expectedCollection: IPaciente[] = [...additionalPacientes, ...pacienteCollection];
      jest.spyOn(pacienteService, 'addPacienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ informe });
      comp.ngOnInit();

      expect(pacienteService.query).toHaveBeenCalled();
      expect(pacienteService.addPacienteToCollectionIfMissing).toHaveBeenCalledWith(
        pacienteCollection,
        ...additionalPacientes.map(expect.objectContaining),
      );
      expect(comp.pacientesSharedCollection).toEqual(expectedCollection);
    });

    it('should call Trabajador query and add missing value', () => {
      const informe: IInforme = { id: 29544 };
      const trabajador: ITrabajador = { id: 528 };
      informe.trabajador = trabajador;

      const trabajadorCollection: ITrabajador[] = [{ id: 528 }];
      jest.spyOn(trabajadorService, 'query').mockReturnValue(of(new HttpResponse({ body: trabajadorCollection })));
      const additionalTrabajadors = [trabajador];
      const expectedCollection: ITrabajador[] = [...additionalTrabajadors, ...trabajadorCollection];
      jest.spyOn(trabajadorService, 'addTrabajadorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ informe });
      comp.ngOnInit();

      expect(trabajadorService.query).toHaveBeenCalled();
      expect(trabajadorService.addTrabajadorToCollectionIfMissing).toHaveBeenCalledWith(
        trabajadorCollection,
        ...additionalTrabajadors.map(expect.objectContaining),
      );
      expect(comp.trabajadorsSharedCollection).toEqual(expectedCollection);
    });

    it('should call Enfermedad query and add missing value', () => {
      const informe: IInforme = { id: 29544 };
      const enfermedads: IEnfermedad[] = [{ id: 11813 }];
      informe.enfermedads = enfermedads;

      const enfermedadCollection: IEnfermedad[] = [{ id: 11813 }];
      jest.spyOn(enfermedadService, 'query').mockReturnValue(of(new HttpResponse({ body: enfermedadCollection })));
      const additionalEnfermedads = [...enfermedads];
      const expectedCollection: IEnfermedad[] = [...additionalEnfermedads, ...enfermedadCollection];
      jest.spyOn(enfermedadService, 'addEnfermedadToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ informe });
      comp.ngOnInit();

      expect(enfermedadService.query).toHaveBeenCalled();
      expect(enfermedadService.addEnfermedadToCollectionIfMissing).toHaveBeenCalledWith(
        enfermedadCollection,
        ...additionalEnfermedads.map(expect.objectContaining),
      );
      expect(comp.enfermedadsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const informe: IInforme = { id: 29544 };
      const receta: IReceta = { id: 15537 };
      informe.receta = receta;
      const paciente: IPaciente = { id: 25847 };
      informe.paciente = paciente;
      const trabajador: ITrabajador = { id: 528 };
      informe.trabajador = trabajador;
      const enfermedad: IEnfermedad = { id: 11813 };
      informe.enfermedads = [enfermedad];

      activatedRoute.data = of({ informe });
      comp.ngOnInit();

      expect(comp.recetasCollection).toContainEqual(receta);
      expect(comp.pacientesSharedCollection).toContainEqual(paciente);
      expect(comp.trabajadorsSharedCollection).toContainEqual(trabajador);
      expect(comp.enfermedadsSharedCollection).toContainEqual(enfermedad);
      expect(comp.informe).toEqual(informe);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInforme>>();
      const informe = { id: 15736 };
      jest.spyOn(informeFormService, 'getInforme').mockReturnValue(informe);
      jest.spyOn(informeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ informe });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: informe }));
      saveSubject.complete();

      // THEN
      expect(informeFormService.getInforme).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(informeService.update).toHaveBeenCalledWith(expect.objectContaining(informe));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInforme>>();
      const informe = { id: 15736 };
      jest.spyOn(informeFormService, 'getInforme').mockReturnValue({ id: null });
      jest.spyOn(informeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ informe: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: informe }));
      saveSubject.complete();

      // THEN
      expect(informeFormService.getInforme).toHaveBeenCalled();
      expect(informeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInforme>>();
      const informe = { id: 15736 };
      jest.spyOn(informeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ informe });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(informeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareReceta', () => {
      it('should forward to recetaService', () => {
        const entity = { id: 15537 };
        const entity2 = { id: 15285 };
        jest.spyOn(recetaService, 'compareReceta');
        comp.compareReceta(entity, entity2);
        expect(recetaService.compareReceta).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareEnfermedad', () => {
      it('should forward to enfermedadService', () => {
        const entity = { id: 11813 };
        const entity2 = { id: 26624 };
        jest.spyOn(enfermedadService, 'compareEnfermedad');
        comp.compareEnfermedad(entity, entity2);
        expect(enfermedadService.compareEnfermedad).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
