import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPlayerDetailPoint, PlayerDetailPoint } from '../player-detail-point.model';

import { PlayerDetailPointService } from './player-detail-point.service';

describe('Service Tests', () => {
  describe('PlayerDetailPoint Service', () => {
    let service: PlayerDetailPointService;
    let httpMock: HttpTestingController;
    let elemDefault: IPlayerDetailPoint;
    let expectedResult: IPlayerDetailPoint | IPlayerDetailPoint[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PlayerDetailPointService);
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

      it('should create a PlayerDetailPoint', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new PlayerDetailPoint()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PlayerDetailPoint', () => {
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

      it('should partial update a PlayerDetailPoint', () => {
        const patchObject = Object.assign(
          {
            points: 1,
          },
          new PlayerDetailPoint()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PlayerDetailPoint', () => {
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

      it('should delete a PlayerDetailPoint', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPlayerDetailPointToCollectionIfMissing', () => {
        it('should add a PlayerDetailPoint to an empty array', () => {
          const playerDetailPoint: IPlayerDetailPoint = { id: 123 };
          expectedResult = service.addPlayerDetailPointToCollectionIfMissing([], playerDetailPoint);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(playerDetailPoint);
        });

        it('should not add a PlayerDetailPoint to an array that contains it', () => {
          const playerDetailPoint: IPlayerDetailPoint = { id: 123 };
          const playerDetailPointCollection: IPlayerDetailPoint[] = [
            {
              ...playerDetailPoint,
            },
            { id: 456 },
          ];
          expectedResult = service.addPlayerDetailPointToCollectionIfMissing(playerDetailPointCollection, playerDetailPoint);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PlayerDetailPoint to an array that doesn't contain it", () => {
          const playerDetailPoint: IPlayerDetailPoint = { id: 123 };
          const playerDetailPointCollection: IPlayerDetailPoint[] = [{ id: 456 }];
          expectedResult = service.addPlayerDetailPointToCollectionIfMissing(playerDetailPointCollection, playerDetailPoint);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(playerDetailPoint);
        });

        it('should add only unique PlayerDetailPoint to an array', () => {
          const playerDetailPointArray: IPlayerDetailPoint[] = [{ id: 123 }, { id: 456 }, { id: 75817 }];
          const playerDetailPointCollection: IPlayerDetailPoint[] = [{ id: 123 }];
          expectedResult = service.addPlayerDetailPointToCollectionIfMissing(playerDetailPointCollection, ...playerDetailPointArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const playerDetailPoint: IPlayerDetailPoint = { id: 123 };
          const playerDetailPoint2: IPlayerDetailPoint = { id: 456 };
          expectedResult = service.addPlayerDetailPointToCollectionIfMissing([], playerDetailPoint, playerDetailPoint2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(playerDetailPoint);
          expect(expectedResult).toContain(playerDetailPoint2);
        });

        it('should accept null and undefined values', () => {
          const playerDetailPoint: IPlayerDetailPoint = { id: 123 };
          expectedResult = service.addPlayerDetailPointToCollectionIfMissing([], null, playerDetailPoint, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(playerDetailPoint);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
