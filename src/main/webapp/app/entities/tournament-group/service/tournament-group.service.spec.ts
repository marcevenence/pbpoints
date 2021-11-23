import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITournamentGroup, TournamentGroup } from '../tournament-group.model';

import { TournamentGroupService } from './tournament-group.service';

describe('Service Tests', () => {
  describe('TournamentGroup Service', () => {
    let service: TournamentGroupService;
    let httpMock: HttpTestingController;
    let elemDefault: ITournamentGroup;
    let expectedResult: ITournamentGroup | ITournamentGroup[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TournamentGroupService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
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

      it('should create a TournamentGroup', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new TournamentGroup()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a TournamentGroup', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a TournamentGroup', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
          },
          new TournamentGroup()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of TournamentGroup', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
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

      it('should delete a TournamentGroup', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTournamentGroupToCollectionIfMissing', () => {
        it('should add a TournamentGroup to an empty array', () => {
          const tournamentGroup: ITournamentGroup = { id: 123 };
          expectedResult = service.addTournamentGroupToCollectionIfMissing([], tournamentGroup);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tournamentGroup);
        });

        it('should not add a TournamentGroup to an array that contains it', () => {
          const tournamentGroup: ITournamentGroup = { id: 123 };
          const tournamentGroupCollection: ITournamentGroup[] = [
            {
              ...tournamentGroup,
            },
            { id: 456 },
          ];
          expectedResult = service.addTournamentGroupToCollectionIfMissing(tournamentGroupCollection, tournamentGroup);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a TournamentGroup to an array that doesn't contain it", () => {
          const tournamentGroup: ITournamentGroup = { id: 123 };
          const tournamentGroupCollection: ITournamentGroup[] = [{ id: 456 }];
          expectedResult = service.addTournamentGroupToCollectionIfMissing(tournamentGroupCollection, tournamentGroup);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tournamentGroup);
        });

        it('should add only unique TournamentGroup to an array', () => {
          const tournamentGroupArray: ITournamentGroup[] = [{ id: 123 }, { id: 456 }, { id: 73209 }];
          const tournamentGroupCollection: ITournamentGroup[] = [{ id: 123 }];
          expectedResult = service.addTournamentGroupToCollectionIfMissing(tournamentGroupCollection, ...tournamentGroupArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const tournamentGroup: ITournamentGroup = { id: 123 };
          const tournamentGroup2: ITournamentGroup = { id: 456 };
          expectedResult = service.addTournamentGroupToCollectionIfMissing([], tournamentGroup, tournamentGroup2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tournamentGroup);
          expect(expectedResult).toContain(tournamentGroup2);
        });

        it('should accept null and undefined values', () => {
          const tournamentGroup: ITournamentGroup = { id: 123 };
          expectedResult = service.addTournamentGroupToCollectionIfMissing([], null, tournamentGroup, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tournamentGroup);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
