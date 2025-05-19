import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IEnfermedad } from '../enfermedad.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../enfermedad.test-samples';

import { EnfermedadService } from './enfermedad.service';

const requireRestSample: IEnfermedad = {
  ...sampleWithRequiredData,
};

describe('Enfermedad Service', () => {
  let service: EnfermedadService;
  let httpMock: HttpTestingController;
  let expectedResult: IEnfermedad | IEnfermedad[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(EnfermedadService);
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

    it('should create a Enfermedad', () => {
      const enfermedad = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(enfermedad).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Enfermedad', () => {
      const enfermedad = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(enfermedad).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Enfermedad', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Enfermedad', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Enfermedad', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEnfermedadToCollectionIfMissing', () => {
      it('should add a Enfermedad to an empty array', () => {
        const enfermedad: IEnfermedad = sampleWithRequiredData;
        expectedResult = service.addEnfermedadToCollectionIfMissing([], enfermedad);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(enfermedad);
      });

      it('should not add a Enfermedad to an array that contains it', () => {
        const enfermedad: IEnfermedad = sampleWithRequiredData;
        const enfermedadCollection: IEnfermedad[] = [
          {
            ...enfermedad,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEnfermedadToCollectionIfMissing(enfermedadCollection, enfermedad);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Enfermedad to an array that doesn't contain it", () => {
        const enfermedad: IEnfermedad = sampleWithRequiredData;
        const enfermedadCollection: IEnfermedad[] = [sampleWithPartialData];
        expectedResult = service.addEnfermedadToCollectionIfMissing(enfermedadCollection, enfermedad);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(enfermedad);
      });

      it('should add only unique Enfermedad to an array', () => {
        const enfermedadArray: IEnfermedad[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const enfermedadCollection: IEnfermedad[] = [sampleWithRequiredData];
        expectedResult = service.addEnfermedadToCollectionIfMissing(enfermedadCollection, ...enfermedadArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const enfermedad: IEnfermedad = sampleWithRequiredData;
        const enfermedad2: IEnfermedad = sampleWithPartialData;
        expectedResult = service.addEnfermedadToCollectionIfMissing([], enfermedad, enfermedad2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(enfermedad);
        expect(expectedResult).toContain(enfermedad2);
      });

      it('should accept null and undefined values', () => {
        const enfermedad: IEnfermedad = sampleWithRequiredData;
        expectedResult = service.addEnfermedadToCollectionIfMissing([], null, enfermedad, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(enfermedad);
      });

      it('should return initial array if no Enfermedad is added', () => {
        const enfermedadCollection: IEnfermedad[] = [sampleWithRequiredData];
        expectedResult = service.addEnfermedadToCollectionIfMissing(enfermedadCollection, undefined, null);
        expect(expectedResult).toEqual(enfermedadCollection);
      });
    });

    describe('compareEnfermedad', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEnfermedad(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 11813 };
        const entity2 = null;

        const compareResult1 = service.compareEnfermedad(entity1, entity2);
        const compareResult2 = service.compareEnfermedad(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 11813 };
        const entity2 = { id: 26624 };

        const compareResult1 = service.compareEnfermedad(entity1, entity2);
        const compareResult2 = service.compareEnfermedad(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 11813 };
        const entity2 = { id: 11813 };

        const compareResult1 = service.compareEnfermedad(entity1, entity2);
        const compareResult2 = service.compareEnfermedad(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
