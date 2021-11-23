jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITournamentGroup, TournamentGroup } from '../tournament-group.model';
import { TournamentGroupService } from '../service/tournament-group.service';

import { TournamentGroupRoutingResolveService } from './tournament-group-routing-resolve.service';

describe('Service Tests', () => {
  describe('TournamentGroup routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TournamentGroupRoutingResolveService;
    let service: TournamentGroupService;
    let resultTournamentGroup: ITournamentGroup | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TournamentGroupRoutingResolveService);
      service = TestBed.inject(TournamentGroupService);
      resultTournamentGroup = undefined;
    });

    describe('resolve', () => {
      it('should return ITournamentGroup returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTournamentGroup = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTournamentGroup).toEqual({ id: 123 });
      });

      it('should return new ITournamentGroup if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTournamentGroup = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTournamentGroup).toEqual(new TournamentGroup());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTournamentGroup = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTournamentGroup).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
