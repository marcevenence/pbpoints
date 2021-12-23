import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITeamCategoryPoint, TeamCategoryPoint } from '../team-category-point.model';

import { TeamCategoryPointService } from './team-category-point.service';

describe('Service Tests', () => {
  describe('TeamCategoryPoint Service', () => {
    let service: TeamCategoryPointService;
    let httpMock: HttpTestingController;
    let elemDefault: ITeamCategoryPoint;
    let expectedResult: ITeamCategoryPoint | ITeamCategoryPoint[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TeamCategoryPointService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        points: 0,
        position: 0,
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

      it('should create a TeamCategoryPoint', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new TeamCategoryPoint()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a TeamCategoryPoint', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            points: 1,
            position: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a TeamCategoryPoint', () => {
        const patchObject = Object.assign(
          {
            position: 1,
          },
          new TeamCategoryPoint()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of TeamCategoryPoint', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            points: 1,
            position: 1,
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

      it('should delete a TeamCategoryPoint', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTeamCategoryPointToCollectionIfMissing', () => {
        it('should add a TeamCategoryPoint to an empty array', () => {
          const teamCategoryPoint: ITeamCategoryPoint = { id: 123 };
          expectedResult = service.addTeamCategoryPointToCollectionIfMissing([], teamCategoryPoint);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(teamCategoryPoint);
        });

        it('should not add a TeamCategoryPoint to an array that contains it', () => {
          const teamCategoryPoint: ITeamCategoryPoint = { id: 123 };
          const teamCategoryPointCollection: ITeamCategoryPoint[] = [
            {
              ...teamCategoryPoint,
            },
            { id: 456 },
          ];
          expectedResult = service.addTeamCategoryPointToCollectionIfMissing(teamCategoryPointCollection, teamCategoryPoint);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a TeamCategoryPoint to an array that doesn't contain it", () => {
          const teamCategoryPoint: ITeamCategoryPoint = { id: 123 };
          const teamCategoryPointCollection: ITeamCategoryPoint[] = [{ id: 456 }];
          expectedResult = service.addTeamCategoryPointToCollectionIfMissing(teamCategoryPointCollection, teamCategoryPoint);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(teamCategoryPoint);
        });

        it('should add only unique TeamCategoryPoint to an array', () => {
          const teamCategoryPointArray: ITeamCategoryPoint[] = [{ id: 123 }, { id: 456 }, { id: 80572 }];
          const teamCategoryPointCollection: ITeamCategoryPoint[] = [{ id: 123 }];
          expectedResult = service.addTeamCategoryPointToCollectionIfMissing(teamCategoryPointCollection, ...teamCategoryPointArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const teamCategoryPoint: ITeamCategoryPoint = { id: 123 };
          const teamCategoryPoint2: ITeamCategoryPoint = { id: 456 };
          expectedResult = service.addTeamCategoryPointToCollectionIfMissing([], teamCategoryPoint, teamCategoryPoint2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(teamCategoryPoint);
          expect(expectedResult).toContain(teamCategoryPoint2);
        });

        it('should accept null and undefined values', () => {
          const teamCategoryPoint: ITeamCategoryPoint = { id: 123 };
          expectedResult = service.addTeamCategoryPointToCollectionIfMissing([], null, teamCategoryPoint, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(teamCategoryPoint);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
