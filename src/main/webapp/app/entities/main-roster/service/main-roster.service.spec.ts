import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMainRoster, MainRoster } from '../main-roster.model';

import { MainRosterService } from './main-roster.service';

describe('Service Tests', () => {
  describe('MainRoster Service', () => {
    let service: MainRosterService;
    let httpMock: HttpTestingController;
    let elemDefault: IMainRoster;
    let expectedResult: IMainRoster | IMainRoster[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MainRosterService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
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

      it('should create a MainRoster', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new MainRoster()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a MainRoster', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a MainRoster', () => {
        const patchObject = Object.assign({}, new MainRoster());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of MainRoster', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
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

      it('should delete a MainRoster', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMainRosterToCollectionIfMissing', () => {
        it('should add a MainRoster to an empty array', () => {
          const mainRoster: IMainRoster = { id: 123 };
          expectedResult = service.addMainRosterToCollectionIfMissing([], mainRoster);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(mainRoster);
        });

        it('should not add a MainRoster to an array that contains it', () => {
          const mainRoster: IMainRoster = { id: 123 };
          const mainRosterCollection: IMainRoster[] = [
            {
              ...mainRoster,
            },
            { id: 456 },
          ];
          expectedResult = service.addMainRosterToCollectionIfMissing(mainRosterCollection, mainRoster);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a MainRoster to an array that doesn't contain it", () => {
          const mainRoster: IMainRoster = { id: 123 };
          const mainRosterCollection: IMainRoster[] = [{ id: 456 }];
          expectedResult = service.addMainRosterToCollectionIfMissing(mainRosterCollection, mainRoster);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(mainRoster);
        });

        it('should add only unique MainRoster to an array', () => {
          const mainRosterArray: IMainRoster[] = [{ id: 123 }, { id: 456 }, { id: 43898 }];
          const mainRosterCollection: IMainRoster[] = [{ id: 123 }];
          expectedResult = service.addMainRosterToCollectionIfMissing(mainRosterCollection, ...mainRosterArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const mainRoster: IMainRoster = { id: 123 };
          const mainRoster2: IMainRoster = { id: 456 };
          expectedResult = service.addMainRosterToCollectionIfMissing([], mainRoster, mainRoster2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(mainRoster);
          expect(expectedResult).toContain(mainRoster2);
        });

        it('should accept null and undefined values', () => {
          const mainRoster: IMainRoster = { id: 123 };
          expectedResult = service.addMainRosterToCollectionIfMissing([], null, mainRoster, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(mainRoster);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
