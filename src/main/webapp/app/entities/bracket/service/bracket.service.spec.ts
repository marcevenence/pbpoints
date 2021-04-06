import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBracket, Bracket } from '../bracket.model';

import { BracketService } from './bracket.service';

describe('Service Tests', () => {
  describe('Bracket Service', () => {
    let service: BracketService;
    let httpMock: HttpTestingController;
    let elemDefault: IBracket;
    let expectedResult: IBracket | IBracket[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(BracketService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        teams: 0,
        teams5A: 0,
        teams5B: 0,
        teams6A: 0,
        teams6B: 0,
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

      it('should create a Bracket', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Bracket()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Bracket', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            teams: 1,
            teams5A: 1,
            teams5B: 1,
            teams6A: 1,
            teams6B: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Bracket', () => {
        const patchObject = Object.assign(
          {
            teams: 1,
            teams5B: 1,
            teams6A: 1,
            teams6B: 1,
          },
          new Bracket()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Bracket', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            teams: 1,
            teams5A: 1,
            teams5B: 1,
            teams6A: 1,
            teams6B: 1,
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

      it('should delete a Bracket', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addBracketToCollectionIfMissing', () => {
        it('should add a Bracket to an empty array', () => {
          const bracket: IBracket = { id: 123 };
          expectedResult = service.addBracketToCollectionIfMissing([], bracket);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(bracket);
        });

        it('should not add a Bracket to an array that contains it', () => {
          const bracket: IBracket = { id: 123 };
          const bracketCollection: IBracket[] = [
            {
              ...bracket,
            },
            { id: 456 },
          ];
          expectedResult = service.addBracketToCollectionIfMissing(bracketCollection, bracket);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Bracket to an array that doesn't contain it", () => {
          const bracket: IBracket = { id: 123 };
          const bracketCollection: IBracket[] = [{ id: 456 }];
          expectedResult = service.addBracketToCollectionIfMissing(bracketCollection, bracket);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(bracket);
        });

        it('should add only unique Bracket to an array', () => {
          const bracketArray: IBracket[] = [{ id: 123 }, { id: 456 }, { id: 15157 }];
          const bracketCollection: IBracket[] = [{ id: 123 }];
          expectedResult = service.addBracketToCollectionIfMissing(bracketCollection, ...bracketArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const bracket: IBracket = { id: 123 };
          const bracket2: IBracket = { id: 456 };
          expectedResult = service.addBracketToCollectionIfMissing([], bracket, bracket2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(bracket);
          expect(expectedResult).toContain(bracket2);
        });

        it('should accept null and undefined values', () => {
          const bracket: IBracket = { id: 123 };
          expectedResult = service.addBracketToCollectionIfMissing([], null, bracket, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(bracket);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
