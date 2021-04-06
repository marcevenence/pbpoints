import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITeamPoint, TeamPoint } from '../team-point.model';

import { TeamPointService } from './team-point.service';

describe('Service Tests', () => {
  describe('TeamPoint Service', () => {
    let service: TeamPointService;
    let httpMock: HttpTestingController;
    let elemDefault: ITeamPoint;
    let expectedResult: ITeamPoint | ITeamPoint[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TeamPointService);
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

      it('should create a TeamPoint', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new TeamPoint()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a TeamPoint', () => {
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

      it('should partial update a TeamPoint', () => {
        const patchObject = Object.assign({}, new TeamPoint());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of TeamPoint', () => {
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

      it('should delete a TeamPoint', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTeamPointToCollectionIfMissing', () => {
        it('should add a TeamPoint to an empty array', () => {
          const teamPoint: ITeamPoint = { id: 123 };
          expectedResult = service.addTeamPointToCollectionIfMissing([], teamPoint);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(teamPoint);
        });

        it('should not add a TeamPoint to an array that contains it', () => {
          const teamPoint: ITeamPoint = { id: 123 };
          const teamPointCollection: ITeamPoint[] = [
            {
              ...teamPoint,
            },
            { id: 456 },
          ];
          expectedResult = service.addTeamPointToCollectionIfMissing(teamPointCollection, teamPoint);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a TeamPoint to an array that doesn't contain it", () => {
          const teamPoint: ITeamPoint = { id: 123 };
          const teamPointCollection: ITeamPoint[] = [{ id: 456 }];
          expectedResult = service.addTeamPointToCollectionIfMissing(teamPointCollection, teamPoint);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(teamPoint);
        });

        it('should add only unique TeamPoint to an array', () => {
          const teamPointArray: ITeamPoint[] = [{ id: 123 }, { id: 456 }, { id: 72448 }];
          const teamPointCollection: ITeamPoint[] = [{ id: 123 }];
          expectedResult = service.addTeamPointToCollectionIfMissing(teamPointCollection, ...teamPointArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const teamPoint: ITeamPoint = { id: 123 };
          const teamPoint2: ITeamPoint = { id: 456 };
          expectedResult = service.addTeamPointToCollectionIfMissing([], teamPoint, teamPoint2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(teamPoint);
          expect(expectedResult).toContain(teamPoint2);
        });

        it('should accept null and undefined values', () => {
          const teamPoint: ITeamPoint = { id: 123 };
          expectedResult = service.addTeamPointToCollectionIfMissing([], null, teamPoint, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(teamPoint);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
