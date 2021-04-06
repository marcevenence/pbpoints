import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFormat, Format } from '../format.model';

import { FormatService } from './format.service';

describe('Service Tests', () => {
  describe('Format Service', () => {
    let service: FormatService;
    let httpMock: HttpTestingController;
    let elemDefault: IFormat;
    let expectedResult: IFormat | IFormat[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FormatService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        description: 'AAAAAAA',
        coeficient: 0,
        playersQty: 0,
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

      it('should create a Format', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Format()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Format', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
            coeficient: 1,
            playersQty: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Format', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
            coeficient: 1,
            playersQty: 1,
          },
          new Format()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Format', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
            coeficient: 1,
            playersQty: 1,
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

      it('should delete a Format', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFormatToCollectionIfMissing', () => {
        it('should add a Format to an empty array', () => {
          const format: IFormat = { id: 123 };
          expectedResult = service.addFormatToCollectionIfMissing([], format);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(format);
        });

        it('should not add a Format to an array that contains it', () => {
          const format: IFormat = { id: 123 };
          const formatCollection: IFormat[] = [
            {
              ...format,
            },
            { id: 456 },
          ];
          expectedResult = service.addFormatToCollectionIfMissing(formatCollection, format);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Format to an array that doesn't contain it", () => {
          const format: IFormat = { id: 123 };
          const formatCollection: IFormat[] = [{ id: 456 }];
          expectedResult = service.addFormatToCollectionIfMissing(formatCollection, format);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(format);
        });

        it('should add only unique Format to an array', () => {
          const formatArray: IFormat[] = [{ id: 123 }, { id: 456 }, { id: 40888 }];
          const formatCollection: IFormat[] = [{ id: 123 }];
          expectedResult = service.addFormatToCollectionIfMissing(formatCollection, ...formatArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const format: IFormat = { id: 123 };
          const format2: IFormat = { id: 456 };
          expectedResult = service.addFormatToCollectionIfMissing([], format, format2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(format);
          expect(expectedResult).toContain(format2);
        });

        it('should accept null and undefined values', () => {
          const format: IFormat = { id: 123 };
          expectedResult = service.addFormatToCollectionIfMissing([], null, format, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(format);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
