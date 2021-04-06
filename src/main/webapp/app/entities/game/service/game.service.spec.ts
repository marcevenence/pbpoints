import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Status } from 'app/entities/enumerations/status.model';
import { IGame, Game } from '../game.model';

import { GameService } from './game.service';

describe('Service Tests', () => {
  describe('Game Service', () => {
    let service: GameService;
    let httpMock: HttpTestingController;
    let elemDefault: IGame;
    let expectedResult: IGame | IGame[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(GameService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        pointsA: 0,
        pointsB: 0,
        splitDeckNum: 0,
        timeLeft: 0,
        status: Status.CREATED,
        overtimeA: 0,
        overtimeB: 0,
        uvuA: 0,
        uvuB: 0,
        group: 0,
        clasif: 'AAAAAAA',
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

      it('should create a Game', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Game()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Game', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            pointsA: 1,
            pointsB: 1,
            splitDeckNum: 1,
            timeLeft: 1,
            status: 'BBBBBB',
            overtimeA: 1,
            overtimeB: 1,
            uvuA: 1,
            uvuB: 1,
            group: 1,
            clasif: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Game', () => {
        const patchObject = Object.assign(
          {
            pointsA: 1,
            pointsB: 1,
            timeLeft: 1,
            status: 'BBBBBB',
            overtimeA: 1,
            uvuA: 1,
            uvuB: 1,
            clasif: 'BBBBBB',
          },
          new Game()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Game', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            pointsA: 1,
            pointsB: 1,
            splitDeckNum: 1,
            timeLeft: 1,
            status: 'BBBBBB',
            overtimeA: 1,
            overtimeB: 1,
            uvuA: 1,
            uvuB: 1,
            group: 1,
            clasif: 'BBBBBB',
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

      it('should delete a Game', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addGameToCollectionIfMissing', () => {
        it('should add a Game to an empty array', () => {
          const game: IGame = { id: 123 };
          expectedResult = service.addGameToCollectionIfMissing([], game);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(game);
        });

        it('should not add a Game to an array that contains it', () => {
          const game: IGame = { id: 123 };
          const gameCollection: IGame[] = [
            {
              ...game,
            },
            { id: 456 },
          ];
          expectedResult = service.addGameToCollectionIfMissing(gameCollection, game);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Game to an array that doesn't contain it", () => {
          const game: IGame = { id: 123 };
          const gameCollection: IGame[] = [{ id: 456 }];
          expectedResult = service.addGameToCollectionIfMissing(gameCollection, game);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(game);
        });

        it('should add only unique Game to an array', () => {
          const gameArray: IGame[] = [{ id: 123 }, { id: 456 }, { id: 82892 }];
          const gameCollection: IGame[] = [{ id: 123 }];
          expectedResult = service.addGameToCollectionIfMissing(gameCollection, ...gameArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const game: IGame = { id: 123 };
          const game2: IGame = { id: 456 };
          expectedResult = service.addGameToCollectionIfMissing([], game, game2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(game);
          expect(expectedResult).toContain(game2);
        });

        it('should accept null and undefined values', () => {
          const game: IGame = { id: 123 };
          expectedResult = service.addGameToCollectionIfMissing([], null, game, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(game);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
