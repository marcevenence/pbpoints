import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITeamDetailPoint, TeamDetailPoint } from '../team-detail-point.model';

import { TeamDetailPointService } from './team-detail-point.service';

describe('Service Tests', () => {
  describe('TeamDetailPoint Service', () => {
    let service: TeamDetailPointService;
    let httpMock: HttpTestingController;
    let elemDefault: ITeamDetailPoint;
    let expectedResult: ITeamDetailPoint | ITeamDetailPoint[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TeamDetailPointService);
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

      it('should create a TeamDetailPoint', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new TeamDetailPoint()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a TeamDetailPoint', () => {
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

      it('should partial update a TeamDetailPoint', () => {
        const patchObject = Object.assign(
          {
            points: 1,
          },
          new TeamDetailPoint()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of TeamDetailPoint', () => {
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

      it('should delete a TeamDetailPoint', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTeamDetailPointToCollectionIfMissing', () => {
        it('should add a TeamDetailPoint to an empty array', () => {
          const teamDetailPoint: ITeamDetailPoint = { id: 123 };
          expectedResult = service.addTeamDetailPointToCollectionIfMissing([], teamDetailPoint);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(teamDetailPoint);
        });

        it('should not add a TeamDetailPoint to an array that contains it', () => {
          const teamDetailPoint: ITeamDetailPoint = { id: 123 };
          const teamDetailPointCollection: ITeamDetailPoint[] = [
            {
              ...teamDetailPoint,
            },
            { id: 456 },
          ];
          expectedResult = service.addTeamDetailPointToCollectionIfMissing(teamDetailPointCollection, teamDetailPoint);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a TeamDetailPoint to an array that doesn't contain it", () => {
          const teamDetailPoint: ITeamDetailPoint = { id: 123 };
          const teamDetailPointCollection: ITeamDetailPoint[] = [{ id: 456 }];
          expectedResult = service.addTeamDetailPointToCollectionIfMissing(teamDetailPointCollection, teamDetailPoint);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(teamDetailPoint);
        });

        it('should add only unique TeamDetailPoint to an array', () => {
          const teamDetailPointArray: ITeamDetailPoint[] = [{ id: 123 }, { id: 456 }, { id: 72844 }];
          const teamDetailPointCollection: ITeamDetailPoint[] = [{ id: 123 }];
          expectedResult = service.addTeamDetailPointToCollectionIfMissing(teamDetailPointCollection, ...teamDetailPointArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const teamDetailPoint: ITeamDetailPoint = { id: 123 };
          const teamDetailPoint2: ITeamDetailPoint = { id: 456 };
          expectedResult = service.addTeamDetailPointToCollectionIfMissing([], teamDetailPoint, teamDetailPoint2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(teamDetailPoint);
          expect(expectedResult).toContain(teamDetailPoint2);
        });

        it('should accept null and undefined values', () => {
          const teamDetailPoint: ITeamDetailPoint = { id: 123 };
          expectedResult = service.addTeamDetailPointToCollectionIfMissing([], null, teamDetailPoint, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(teamDetailPoint);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
