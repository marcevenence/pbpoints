import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFormula, Formula } from '../formula.model';

import { FormulaService } from './formula.service';

describe('Service Tests', () => {
  describe('Formula Service', () => {
    let service: FormulaService;
    let httpMock: HttpTestingController;
    let elemDefault: IFormula;
    let expectedResult: IFormula | IFormula[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FormulaService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        formula: 'AAAAAAA',
        var1: 'AAAAAAA',
        var2: 'AAAAAAA',
        var3: 'AAAAAAA',
        description: 'AAAAAAA',
        example: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Formula', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Formula()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Formula', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            formula: 'BBBBBB',
            var1: 'BBBBBB',
            var2: 'BBBBBB',
            var3: 'BBBBBB',
            description: 'BBBBBB',
            example: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Formula', () => {
        const patchObject = Object.assign(
          {
            var2: 'BBBBBB',
            var3: 'BBBBBB',
          },
          new Formula()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Formula', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            formula: 'BBBBBB',
            var1: 'BBBBBB',
            var2: 'BBBBBB',
            var3: 'BBBBBB',
            description: 'BBBBBB',
            example: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Formula', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFormulaToCollectionIfMissing', () => {
        it('should add a Formula to an empty array', () => {
          const formula: IFormula = { id: 123 };
          expectedResult = service.addFormulaToCollectionIfMissing([], formula);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(formula);
        });

        it('should not add a Formula to an array that contains it', () => {
          const formula: IFormula = { id: 123 };
          const formulaCollection: IFormula[] = [
            {
              ...formula,
            },
            { id: 456 },
          ];
          expectedResult = service.addFormulaToCollectionIfMissing(formulaCollection, formula);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Formula to an array that doesn't contain it", () => {
          const formula: IFormula = { id: 123 };
          const formulaCollection: IFormula[] = [{ id: 456 }];
          expectedResult = service.addFormulaToCollectionIfMissing(formulaCollection, formula);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(formula);
        });

        it('should add only unique Formula to an array', () => {
          const formulaArray: IFormula[] = [{ id: 123 }, { id: 456 }, { id: 25202 }];
          const formulaCollection: IFormula[] = [{ id: 123 }];
          expectedResult = service.addFormulaToCollectionIfMissing(formulaCollection, ...formulaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const formula: IFormula = { id: 123 };
          const formula2: IFormula = { id: 456 };
          expectedResult = service.addFormulaToCollectionIfMissing([], formula, formula2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(formula);
          expect(expectedResult).toContain(formula2);
        });

        it('should accept null and undefined values', () => {
          const formula: IFormula = { id: 123 };
          expectedResult = service.addFormulaToCollectionIfMissing([], null, formula, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(formula);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
