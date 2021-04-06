import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPlayerPoint, PlayerPoint } from '../player-point.model';

import { PlayerPointService } from './player-point.service';

describe('Service Tests', () => {
  describe('PlayerPoint Service', () => {
    let service: PlayerPointService;
    let httpMock: HttpTestingController;
    let elemDefault: IPlayerPoint;
    let expectedResult: IPlayerPoint | IPlayerPoint[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PlayerPointService);
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

      it('should create a PlayerPoint', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new PlayerPoint()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PlayerPoint', () => {
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

      it('should partial update a PlayerPoint', () => {
        const patchObject = Object.assign({}, new PlayerPoint());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PlayerPoint', () => {
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

      it('should delete a PlayerPoint', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPlayerPointToCollectionIfMissing', () => {
        it('should add a PlayerPoint to an empty array', () => {
          const playerPoint: IPlayerPoint = { id: 123 };
          expectedResult = service.addPlayerPointToCollectionIfMissing([], playerPoint);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(playerPoint);
        });

        it('should not add a PlayerPoint to an array that contains it', () => {
          const playerPoint: IPlayerPoint = { id: 123 };
          const playerPointCollection: IPlayerPoint[] = [
            {
              ...playerPoint,
            },
            { id: 456 },
          ];
          expectedResult = service.addPlayerPointToCollectionIfMissing(playerPointCollection, playerPoint);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PlayerPoint to an array that doesn't contain it", () => {
          const playerPoint: IPlayerPoint = { id: 123 };
          const playerPointCollection: IPlayerPoint[] = [{ id: 456 }];
          expectedResult = service.addPlayerPointToCollectionIfMissing(playerPointCollection, playerPoint);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(playerPoint);
        });

        it('should add only unique PlayerPoint to an array', () => {
          const playerPointArray: IPlayerPoint[] = [{ id: 123 }, { id: 456 }, { id: 96698 }];
          const playerPointCollection: IPlayerPoint[] = [{ id: 123 }];
          expectedResult = service.addPlayerPointToCollectionIfMissing(playerPointCollection, ...playerPointArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const playerPoint: IPlayerPoint = { id: 123 };
          const playerPoint2: IPlayerPoint = { id: 456 };
          expectedResult = service.addPlayerPointToCollectionIfMissing([], playerPoint, playerPoint2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(playerPoint);
          expect(expectedResult).toContain(playerPoint2);
        });

        it('should accept null and undefined values', () => {
          const playerPoint: IPlayerPoint = { id: 123 };
          expectedResult = service.addPlayerPointToCollectionIfMissing([], null, playerPoint, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(playerPoint);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
