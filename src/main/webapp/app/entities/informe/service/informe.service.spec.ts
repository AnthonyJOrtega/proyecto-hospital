import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IInforme } from '../informe.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../informe.test-samples';

import { InformeService } from './informe.service';

const requireRestSample: IInforme = {
  ...sampleWithRequiredData,
};

describe('Informe Service', () => {
  let service: InformeService;
  let httpMock: HttpTestingController;
  let expectedResult: IInforme | IInforme[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(InformeService);
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

    it('should create a Informe', () => {
      const informe = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(informe).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Informe', () => {
      const informe = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(informe).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Informe', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Informe', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Informe', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addInformeToCollectionIfMissing', () => {
      it('should add a Informe to an empty array', () => {
        const informe: IInforme = sampleWithRequiredData;
        expectedResult = service.addInformeToCollectionIfMissing([], informe);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(informe);
      });

      it('should not add a Informe to an array that contains it', () => {
        const informe: IInforme = sampleWithRequiredData;
        const informeCollection: IInforme[] = [
          {
            ...informe,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInformeToCollectionIfMissing(informeCollection, informe);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Informe to an array that doesn't contain it", () => {
        const informe: IInforme = sampleWithRequiredData;
        const informeCollection: IInforme[] = [sampleWithPartialData];
        expectedResult = service.addInformeToCollectionIfMissing(informeCollection, informe);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(informe);
      });

      it('should add only unique Informe to an array', () => {
        const informeArray: IInforme[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const informeCollection: IInforme[] = [sampleWithRequiredData];
        expectedResult = service.addInformeToCollectionIfMissing(informeCollection, ...informeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const informe: IInforme = sampleWithRequiredData;
        const informe2: IInforme = sampleWithPartialData;
        expectedResult = service.addInformeToCollectionIfMissing([], informe, informe2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(informe);
        expect(expectedResult).toContain(informe2);
      });

      it('should accept null and undefined values', () => {
        const informe: IInforme = sampleWithRequiredData;
        expectedResult = service.addInformeToCollectionIfMissing([], null, informe, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(informe);
      });

      it('should return initial array if no Informe is added', () => {
        const informeCollection: IInforme[] = [sampleWithRequiredData];
        expectedResult = service.addInformeToCollectionIfMissing(informeCollection, undefined, null);
        expect(expectedResult).toEqual(informeCollection);
      });
    });

    describe('compareInforme', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInforme(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 15736 };
        const entity2 = null;

        const compareResult1 = service.compareInforme(entity1, entity2);
        const compareResult2 = service.compareInforme(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 15736 };
        const entity2 = { id: 29544 };

        const compareResult1 = service.compareInforme(entity1, entity2);
        const compareResult2 = service.compareInforme(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 15736 };
        const entity2 = { id: 15736 };

        const compareResult1 = service.compareInforme(entity1, entity2);
        const compareResult2 = service.compareInforme(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
