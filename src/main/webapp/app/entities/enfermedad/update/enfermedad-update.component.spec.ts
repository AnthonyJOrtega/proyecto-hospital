import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IInforme } from 'app/entities/informe/informe.model';
import { InformeService } from 'app/entities/informe/service/informe.service';
import { EnfermedadService } from '../service/enfermedad.service';
import { IEnfermedad } from '../enfermedad.model';
import { EnfermedadFormService } from './enfermedad-form.service';

import { EnfermedadUpdateComponent } from './enfermedad-update.component';

describe('Enfermedad Management Update Component', () => {
  let comp: EnfermedadUpdateComponent;
  let fixture: ComponentFixture<EnfermedadUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let enfermedadFormService: EnfermedadFormService;
  let enfermedadService: EnfermedadService;
  let informeService: InformeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EnfermedadUpdateComponent],
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
      .overrideTemplate(EnfermedadUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EnfermedadUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    enfermedadFormService = TestBed.inject(EnfermedadFormService);
    enfermedadService = TestBed.inject(EnfermedadService);
    informeService = TestBed.inject(InformeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Informe query and add missing value', () => {
      const enfermedad: IEnfermedad = { id: 26624 };
      const informes: IInforme[] = [{ id: 15736 }];
      enfermedad.informes = informes;

      const informeCollection: IInforme[] = [{ id: 15736 }];
      jest.spyOn(informeService, 'query').mockReturnValue(of(new HttpResponse({ body: informeCollection })));
      const additionalInformes = [...informes];
      const expectedCollection: IInforme[] = [...additionalInformes, ...informeCollection];
      jest.spyOn(informeService, 'addInformeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ enfermedad });
      comp.ngOnInit();

      expect(informeService.query).toHaveBeenCalled();
      expect(informeService.addInformeToCollectionIfMissing).toHaveBeenCalledWith(
        informeCollection,
        ...additionalInformes.map(expect.objectContaining),
      );
      expect(comp.informesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const enfermedad: IEnfermedad = { id: 26624 };
      const informe: IInforme = { id: 15736 };
      enfermedad.informes = [informe];

      activatedRoute.data = of({ enfermedad });
      comp.ngOnInit();

      expect(comp.informesSharedCollection).toContainEqual(informe);
      expect(comp.enfermedad).toEqual(enfermedad);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEnfermedad>>();
      const enfermedad = { id: 11813 };
      jest.spyOn(enfermedadFormService, 'getEnfermedad').mockReturnValue(enfermedad);
      jest.spyOn(enfermedadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ enfermedad });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: enfermedad }));
      saveSubject.complete();

      // THEN
      expect(enfermedadFormService.getEnfermedad).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(enfermedadService.update).toHaveBeenCalledWith(expect.objectContaining(enfermedad));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEnfermedad>>();
      const enfermedad = { id: 11813 };
      jest.spyOn(enfermedadFormService, 'getEnfermedad').mockReturnValue({ id: null });
      jest.spyOn(enfermedadService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ enfermedad: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: enfermedad }));
      saveSubject.complete();

      // THEN
      expect(enfermedadFormService.getEnfermedad).toHaveBeenCalled();
      expect(enfermedadService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEnfermedad>>();
      const enfermedad = { id: 11813 };
      jest.spyOn(enfermedadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ enfermedad });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(enfermedadService.update).toHaveBeenCalled();
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
  });
});
