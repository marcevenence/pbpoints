jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITeamCategoryPoint, TeamCategoryPoint } from '../team-category-point.model';
import { TeamCategoryPointService } from '../service/team-category-point.service';

import { TeamCategoryPointRoutingResolveService } from './team-category-point-routing-resolve.service';

describe('Service Tests', () => {
  describe('TeamCategoryPoint routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TeamCategoryPointRoutingResolveService;
    let service: TeamCategoryPointService;
    let resultTeamCategoryPoint: ITeamCategoryPoint | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TeamCategoryPointRoutingResolveService);
      service = TestBed.inject(TeamCategoryPointService);
      resultTeamCategoryPoint = undefined;
    });

    describe('resolve', () => {
      it('should return ITeamCategoryPoint returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTeamCategoryPoint = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTeamCategoryPoint).toEqual({ id: 123 });
      });

      it('should return new ITeamCategoryPoint if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTeamCategoryPoint = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTeamCategoryPoint).toEqual(new TeamCategoryPoint());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTeamCategoryPoint = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTeamCategoryPoint).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
