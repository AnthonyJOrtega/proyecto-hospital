import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IReceta } from 'app/entities/receta/receta.model';
import { RecetaService } from 'app/entities/receta/service/receta.service';
import { MedicamentoService } from '../service/medicamento.service';
import { IMedicamento } from '../medicamento.model';
import { MedicamentoFormService } from './medicamento-form.service';

import { MedicamentoUpdateComponent } from './medicamento-update.component';

describe('Medicamento Management Update Component', () => {
  let comp: MedicamentoUpdateComponent;
  let fixture: ComponentFixture<MedicamentoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let medicamentoFormService: MedicamentoFormService;
  let medicamentoService: MedicamentoService;
  let recetaService: RecetaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MedicamentoUpdateComponent],
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
      .overrideTemplate(MedicamentoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MedicamentoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    medicamentoFormService = TestBed.inject(MedicamentoFormService);
    medicamentoService = TestBed.inject(MedicamentoService);
    recetaService = TestBed.inject(RecetaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Receta query and add missing value', () => {
      const medicamento: IMedicamento = { id: 10825 };
      const recetas: IReceta[] = [{ id: 15537 }];
      medicamento.recetas = recetas;

      const recetaCollection: IReceta[] = [{ id: 15537 }];
      jest.spyOn(recetaService, 'query').mockReturnValue(of(new HttpResponse({ body: recetaCollection })));
      const additionalRecetas = [...recetas];
      const expectedCollection: IReceta[] = [...additionalRecetas, ...recetaCollection];
      jest.spyOn(recetaService, 'addRecetaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ medicamento });
      comp.ngOnInit();

      expect(recetaService.query).toHaveBeenCalled();
      expect(recetaService.addRecetaToCollectionIfMissing).toHaveBeenCalledWith(
        recetaCollection,
        ...additionalRecetas.map(expect.objectContaining),
      );
      expect(comp.recetasSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const medicamento: IMedicamento = { id: 10825 };
      const receta: IReceta = { id: 15537 };
      medicamento.recetas = [receta];

      activatedRoute.data = of({ medicamento });
      comp.ngOnInit();

      expect(comp.recetasSharedCollection).toContainEqual(receta);
      expect(comp.medicamento).toEqual(medicamento);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedicamento>>();
      const medicamento = { id: 6895 };
      jest.spyOn(medicamentoFormService, 'getMedicamento').mockReturnValue(medicamento);
      jest.spyOn(medicamentoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicamento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: medicamento }));
      saveSubject.complete();

      // THEN
      expect(medicamentoFormService.getMedicamento).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(medicamentoService.update).toHaveBeenCalledWith(expect.objectContaining(medicamento));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedicamento>>();
      const medicamento = { id: 6895 };
      jest.spyOn(medicamentoFormService, 'getMedicamento').mockReturnValue({ id: null });
      jest.spyOn(medicamentoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicamento: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: medicamento }));
      saveSubject.complete();

      // THEN
      expect(medicamentoFormService.getMedicamento).toHaveBeenCalled();
      expect(medicamentoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedicamento>>();
      const medicamento = { id: 6895 };
      jest.spyOn(medicamentoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicamento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(medicamentoService.update).toHaveBeenCalled();
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
  });
});
