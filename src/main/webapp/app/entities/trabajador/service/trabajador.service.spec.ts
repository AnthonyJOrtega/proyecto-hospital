import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ITrabajador } from '../trabajador.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../trabajador.test-samples';

import { TrabajadorService } from './trabajador.service';

const requireRestSample: ITrabajador = {
  ...sampleWithRequiredData,
};

describe('Trabajador Service', () => {
  let service: TrabajadorService;
  let httpMock: HttpTestingController;
  let expectedResult: ITrabajador | ITrabajador[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TrabajadorService);
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

    it('should create a Trabajador', () => {
      const trabajador = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(trabajador).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Trabajador', () => {
      const trabajador = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(trabajador).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Trabajador', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Trabajador', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Trabajador', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTrabajadorToCollectionIfMissing', () => {
      it('should add a Trabajador to an empty array', () => {
        const trabajador: ITrabajador = sampleWithRequiredData;
        expectedResult = service.addTrabajadorToCollectionIfMissing([], trabajador);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trabajador);
      });

      it('should not add a Trabajador to an array that contains it', () => {
        const trabajador: ITrabajador = sampleWithRequiredData;
        const trabajadorCollection: ITrabajador[] = [
          {
            ...trabajador,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTrabajadorToCollectionIfMissing(trabajadorCollection, trabajador);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Trabajador to an array that doesn't contain it", () => {
        const trabajador: ITrabajador = sampleWithRequiredData;
        const trabajadorCollection: ITrabajador[] = [sampleWithPartialData];
        expectedResult = service.addTrabajadorToCollectionIfMissing(trabajadorCollection, trabajador);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trabajador);
      });

      it('should add only unique Trabajador to an array', () => {
        const trabajadorArray: ITrabajador[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const trabajadorCollection: ITrabajador[] = [sampleWithRequiredData];
        expectedResult = service.addTrabajadorToCollectionIfMissing(trabajadorCollection, ...trabajadorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const trabajador: ITrabajador = sampleWithRequiredData;
        const trabajador2: ITrabajador = sampleWithPartialData;
        expectedResult = service.addTrabajadorToCollectionIfMissing([], trabajador, trabajador2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trabajador);
        expect(expectedResult).toContain(trabajador2);
      });

      it('should accept null and undefined values', () => {
        const trabajador: ITrabajador = sampleWithRequiredData;
        expectedResult = service.addTrabajadorToCollectionIfMissing([], null, trabajador, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trabajador);
      });

      it('should return initial array if no Trabajador is added', () => {
        const trabajadorCollection: ITrabajador[] = [sampleWithRequiredData];
        expectedResult = service.addTrabajadorToCollectionIfMissing(trabajadorCollection, undefined, null);
        expect(expectedResult).toEqual(trabajadorCollection);
      });
    });

    describe('compareTrabajador', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTrabajador(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 528 };
        const entity2 = null;

        const compareResult1 = service.compareTrabajador(entity1, entity2);
        const compareResult2 = service.compareTrabajador(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 528 };
        const entity2 = { id: 4393 };

        const compareResult1 = service.compareTrabajador(entity1, entity2);
        const compareResult2 = service.compareTrabajador(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 528 };
        const entity2 = { id: 528 };

        const compareResult1 = service.compareTrabajador(entity1, entity2);
        const compareResult2 = service.compareTrabajador(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
