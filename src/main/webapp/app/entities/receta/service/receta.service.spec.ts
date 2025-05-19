import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IReceta } from '../receta.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../receta.test-samples';

import { RecetaService, RestReceta } from './receta.service';

const requireRestSample: RestReceta = {
  ...sampleWithRequiredData,
  fechaInicio: sampleWithRequiredData.fechaInicio?.format(DATE_FORMAT),
  fechaFin: sampleWithRequiredData.fechaFin?.format(DATE_FORMAT),
};

describe('Receta Service', () => {
  let service: RecetaService;
  let httpMock: HttpTestingController;
  let expectedResult: IReceta | IReceta[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(RecetaService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Receta', () => {
      const receta = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(receta).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Receta', () => {
      const receta = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(receta).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Receta', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Receta', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Receta', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRecetaToCollectionIfMissing', () => {
      it('should add a Receta to an empty array', () => {
        const receta: IReceta = sampleWithRequiredData;
        expectedResult = service.addRecetaToCollectionIfMissing([], receta);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(receta);
      });

      it('should not add a Receta to an array that contains it', () => {
        const receta: IReceta = sampleWithRequiredData;
        const recetaCollection: IReceta[] = [
          {
            ...receta,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRecetaToCollectionIfMissing(recetaCollection, receta);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Receta to an array that doesn't contain it", () => {
        const receta: IReceta = sampleWithRequiredData;
        const recetaCollection: IReceta[] = [sampleWithPartialData];
        expectedResult = service.addRecetaToCollectionIfMissing(recetaCollection, receta);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(receta);
      });

      it('should add only unique Receta to an array', () => {
        const recetaArray: IReceta[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const recetaCollection: IReceta[] = [sampleWithRequiredData];
        expectedResult = service.addRecetaToCollectionIfMissing(recetaCollection, ...recetaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const receta: IReceta = sampleWithRequiredData;
        const receta2: IReceta = sampleWithPartialData;
        expectedResult = service.addRecetaToCollectionIfMissing([], receta, receta2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(receta);
        expect(expectedResult).toContain(receta2);
      });

      it('should accept null and undefined values', () => {
        const receta: IReceta = sampleWithRequiredData;
        expectedResult = service.addRecetaToCollectionIfMissing([], null, receta, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(receta);
      });

      it('should return initial array if no Receta is added', () => {
        const recetaCollection: IReceta[] = [sampleWithRequiredData];
        expectedResult = service.addRecetaToCollectionIfMissing(recetaCollection, undefined, null);
        expect(expectedResult).toEqual(recetaCollection);
      });
    });

    describe('compareReceta', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareReceta(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 15537 };
        const entity2 = null;

        const compareResult1 = service.compareReceta(entity1, entity2);
        const compareResult2 = service.compareReceta(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 15537 };
        const entity2 = { id: 15285 };

        const compareResult1 = service.compareReceta(entity1, entity2);
        const compareResult2 = service.compareReceta(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 15537 };
        const entity2 = { id: 15537 };

        const compareResult1 = service.compareReceta(entity1, entity2);
        const compareResult2 = service.compareReceta(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
