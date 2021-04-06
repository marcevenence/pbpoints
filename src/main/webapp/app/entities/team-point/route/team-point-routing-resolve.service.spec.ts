jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITeamPoint, TeamPoint } from '../team-point.model';
import { TeamPointService } from '../service/team-point.service';

import { TeamPointRoutingResolveService } from './team-point-routing-resolve.service';

describe('Service Tests', () => {
  describe('TeamPoint routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TeamPointRoutingResolveService;
    let service: TeamPointService;
    let resultTeamPoint: ITeamPoint | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TeamPointRoutingResolveService);
      service = TestBed.inject(TeamPointService);
      resultTeamPoint = undefined;
    });

    describe('resolve', () => {
      it('should return ITeamPoint returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTeamPoint = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTeamPoint).toEqual({ id: 123 });
      });

      it('should return new ITeamPoint if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTeamPoint = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTeamPoint).toEqual(new TeamPoint());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTeamPoint = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTeamPoint).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
