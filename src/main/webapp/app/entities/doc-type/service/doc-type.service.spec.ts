import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDocType, DocType } from '../doc-type.model';

import { DocTypeService } from './doc-type.service';

describe('Service Tests', () => {
  describe('DocType Service', () => {
    let service: DocTypeService;
    let httpMock: HttpTestingController;
    let elemDefault: IDocType;
    let expectedResult: IDocType | IDocType[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DocTypeService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        description: 'AAAAAAA',
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

      it('should create a DocType', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new DocType()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a DocType', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a DocType', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
          },
          new DocType()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of DocType', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
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

      it('should delete a DocType', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDocTypeToCollectionIfMissing', () => {
        it('should add a DocType to an empty array', () => {
          const docType: IDocType = { id: 123 };
          expectedResult = service.addDocTypeToCollectionIfMissing([], docType);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(docType);
        });

        it('should not add a DocType to an array that contains it', () => {
          const docType: IDocType = { id: 123 };
          const docTypeCollection: IDocType[] = [
            {
              ...docType,
            },
            { id: 456 },
          ];
          expectedResult = service.addDocTypeToCollectionIfMissing(docTypeCollection, docType);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a DocType to an array that doesn't contain it", () => {
          const docType: IDocType = { id: 123 };
          const docTypeCollection: IDocType[] = [{ id: 456 }];
          expectedResult = service.addDocTypeToCollectionIfMissing(docTypeCollection, docType);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(docType);
        });

        it('should add only unique DocType to an array', () => {
          const docTypeArray: IDocType[] = [{ id: 123 }, { id: 456 }, { id: 94538 }];
          const docTypeCollection: IDocType[] = [{ id: 123 }];
          expectedResult = service.addDocTypeToCollectionIfMissing(docTypeCollection, ...docTypeArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const docType: IDocType = { id: 123 };
          const docType2: IDocType = { id: 456 };
          expectedResult = service.addDocTypeToCollectionIfMissing([], docType, docType2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(docType);
          expect(expectedResult).toContain(docType2);
        });

        it('should accept null and undefined values', () => {
          const docType: IDocType = { id: 123 };
          expectedResult = service.addDocTypeToCollectionIfMissing([], null, docType, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(docType);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
