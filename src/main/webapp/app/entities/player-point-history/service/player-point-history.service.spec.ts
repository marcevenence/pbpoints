import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPlayerPointHistory, PlayerPointHistory } from '../player-point-history.model';

import { PlayerPointHistoryService } from './player-point-history.service';

describe('PlayerPointHistory Service', () => {
  let service: PlayerPointHistoryService;
  let httpMock: HttpTestingController;
  let elemDefault: IPlayerPointHistory;
  let expectedResult: IPlayerPointHistory | IPlayerPointHistory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PlayerPointHistoryService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      points: 0,
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

    it('should create a PlayerPointHistory', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new PlayerPointHistory()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PlayerPointHistory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          points: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PlayerPointHistory', () => {
      const patchObject = Object.assign(
        {
          points: 1,
        },
        new PlayerPointHistory()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PlayerPointHistory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          points: 1,
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

    it('should delete a PlayerPointHistory', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPlayerPointHistoryToCollectionIfMissing', () => {
      it('should add a PlayerPointHistory to an empty array', () => {
        const playerPointHistory: IPlayerPointHistory = { id: 123 };
        expectedResult = service.addPlayerPointHistoryToCollectionIfMissing([], playerPointHistory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(playerPointHistory);
      });

      it('should not add a PlayerPointHistory to an array that contains it', () => {
        const playerPointHistory: IPlayerPointHistory = { id: 123 };
        const playerPointHistoryCollection: IPlayerPointHistory[] = [
          {
            ...playerPointHistory,
          },
          { id: 456 },
        ];
        expectedResult = service.addPlayerPointHistoryToCollectionIfMissing(playerPointHistoryCollection, playerPointHistory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PlayerPointHistory to an array that doesn't contain it", () => {
        const playerPointHistory: IPlayerPointHistory = { id: 123 };
        const playerPointHistoryCollection: IPlayerPointHistory[] = [{ id: 456 }];
        expectedResult = service.addPlayerPointHistoryToCollectionIfMissing(playerPointHistoryCollection, playerPointHistory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(playerPointHistory);
      });

      it('should add only unique PlayerPointHistory to an array', () => {
        const playerPointHistoryArray: IPlayerPointHistory[] = [{ id: 123 }, { id: 456 }, { id: 94811 }];
        const playerPointHistoryCollection: IPlayerPointHistory[] = [{ id: 123 }];
        expectedResult = service.addPlayerPointHistoryToCollectionIfMissing(playerPointHistoryCollection, ...playerPointHistoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const playerPointHistory: IPlayerPointHistory = { id: 123 };
        const playerPointHistory2: IPlayerPointHistory = { id: 456 };
        expectedResult = service.addPlayerPointHistoryToCollectionIfMissing([], playerPointHistory, playerPointHistory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(playerPointHistory);
        expect(expectedResult).toContain(playerPointHistory2);
      });

      it('should accept null and undefined values', () => {
        const playerPointHistory: IPlayerPointHistory = { id: 123 };
        expectedResult = service.addPlayerPointHistoryToCollectionIfMissing([], null, playerPointHistory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(playerPointHistory);
      });

      it('should return initial array if no PlayerPointHistory is added', () => {
        const playerPointHistoryCollection: IPlayerPointHistory[] = [{ id: 123 }];
        expectedResult = service.addPlayerPointHistoryToCollectionIfMissing(playerPointHistoryCollection, undefined, null);
        expect(expectedResult).toEqual(playerPointHistoryCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
