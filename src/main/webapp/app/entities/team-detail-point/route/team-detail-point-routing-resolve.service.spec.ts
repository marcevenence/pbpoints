jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITeamDetailPoint, TeamDetailPoint } from '../team-detail-point.model';
import { TeamDetailPointService } from '../service/team-detail-point.service';

import { TeamDetailPointRoutingResolveService } from './team-detail-point-routing-resolve.service';

describe('Service Tests', () => {
  describe('TeamDetailPoint routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TeamDetailPointRoutingResolveService;
    let service: TeamDetailPointService;
    let resultTeamDetailPoint: ITeamDetailPoint | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TeamDetailPointRoutingResolveService);
      service = TestBed.inject(TeamDetailPointService);
      resultTeamDetailPoint = undefined;
    });

    describe('resolve', () => {
      it('should return ITeamDetailPoint returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTeamDetailPoint = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTeamDetailPoint).toEqual({ id: 123 });
      });

      it('should return new ITeamDetailPoint if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTeamDetailPoint = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTeamDetailPoint).toEqual(new TeamDetailPoint());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTeamDetailPoint = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTeamDetailPoint).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
