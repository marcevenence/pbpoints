import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRoster, Roster } from '../roster.model';

import { RosterService } from './roster.service';

describe('Service Tests', () => {
  describe('Roster Service', () => {
    let service: RosterService;
    let httpMock: HttpTestingController;
    let elemDefault: IRoster;
    let expectedResult: IRoster | IRoster[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(RosterService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        active: false,
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

      it('should create a Roster', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Roster()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Roster', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            active: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Roster', () => {
        const patchObject = Object.assign(
          {
            active: true,
          },
          new Roster()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Roster', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            active: true,
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

      it('should delete a Roster', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addRosterToCollectionIfMissing', () => {
        it('should add a Roster to an empty array', () => {
          const roster: IRoster = { id: 123 };
          expectedResult = service.addRosterToCollectionIfMissing([], roster);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(roster);
        });

        it('should not add a Roster to an array that contains it', () => {
          const roster: IRoster = { id: 123 };
          const rosterCollection: IRoster[] = [
            {
              ...roster,
            },
            { id: 456 },
          ];
          expectedResult = service.addRosterToCollectionIfMissing(rosterCollection, roster);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Roster to an array that doesn't contain it", () => {
          const roster: IRoster = { id: 123 };
          const rosterCollection: IRoster[] = [{ id: 456 }];
          expectedResult = service.addRosterToCollectionIfMissing(rosterCollection, roster);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(roster);
        });

        it('should add only unique Roster to an array', () => {
          const rosterArray: IRoster[] = [{ id: 123 }, { id: 456 }, { id: 95681 }];
          const rosterCollection: IRoster[] = [{ id: 123 }];
          expectedResult = service.addRosterToCollectionIfMissing(rosterCollection, ...rosterArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const roster: IRoster = { id: 123 };
          const roster2: IRoster = { id: 456 };
          expectedResult = service.addRosterToCollectionIfMissing([], roster, roster2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(roster);
          expect(expectedResult).toContain(roster2);
        });

        it('should accept null and undefined values', () => {
          const roster: IRoster = { id: 123 };
          expectedResult = service.addRosterToCollectionIfMissing([], null, roster, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(roster);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
