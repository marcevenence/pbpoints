import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Status } from 'app/entities/enumerations/status.model';
import { ITournament, Tournament } from '../tournament.model';

import { TournamentService } from './tournament.service';

describe('Service Tests', () => {
  describe('Tournament Service', () => {
    let service: TournamentService;
    let httpMock: HttpTestingController;
    let elemDefault: ITournament;
    let expectedResult: ITournament | ITournament[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TournamentService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        closeInscrDays: 0,
        status: Status.CREATED,
        categorize: false,
        logoContentType: 'image/png',
        logo: 'AAAAAAA',
        cantPlayersNextCategory: 0,
        qtyTeamGroups: 0,
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

      it('should create a Tournament', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Tournament()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Tournament', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            closeInscrDays: 1,
            status: 'BBBBBB',
            categorize: true,
            logo: 'BBBBBB',
            cantPlayersNextCategory: 1,
            qtyTeamGroups: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Tournament', () => {
        const patchObject = Object.assign(
          {
            closeInscrDays: 1,
            status: 'BBBBBB',
            categorize: true,
            cantPlayersNextCategory: 1,
            qtyTeamGroups: 1,
          },
          new Tournament()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Tournament', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            closeInscrDays: 1,
            status: 'BBBBBB',
            categorize: true,
            logo: 'BBBBBB',
            cantPlayersNextCategory: 1,
            qtyTeamGroups: 1,
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

      it('should delete a Tournament', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTournamentToCollectionIfMissing', () => {
        it('should add a Tournament to an empty array', () => {
          const tournament: ITournament = { id: 123 };
          expectedResult = service.addTournamentToCollectionIfMissing([], tournament);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tournament);
        });

        it('should not add a Tournament to an array that contains it', () => {
          const tournament: ITournament = { id: 123 };
          const tournamentCollection: ITournament[] = [
            {
              ...tournament,
            },
            { id: 456 },
          ];
          expectedResult = service.addTournamentToCollectionIfMissing(tournamentCollection, tournament);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Tournament to an array that doesn't contain it", () => {
          const tournament: ITournament = { id: 123 };
          const tournamentCollection: ITournament[] = [{ id: 456 }];
          expectedResult = service.addTournamentToCollectionIfMissing(tournamentCollection, tournament);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tournament);
        });

        it('should add only unique Tournament to an array', () => {
          const tournamentArray: ITournament[] = [{ id: 123 }, { id: 456 }, { id: 63503 }];
          const tournamentCollection: ITournament[] = [{ id: 123 }];
          expectedResult = service.addTournamentToCollectionIfMissing(tournamentCollection, ...tournamentArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const tournament: ITournament = { id: 123 };
          const tournament2: ITournament = { id: 456 };
          expectedResult = service.addTournamentToCollectionIfMissing([], tournament, tournament2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tournament);
          expect(expectedResult).toContain(tournament2);
        });

        it('should accept null and undefined values', () => {
          const tournament: ITournament = { id: 123 };
          expectedResult = service.addTournamentToCollectionIfMissing([], null, tournament, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tournament);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
