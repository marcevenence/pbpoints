jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITournament, Tournament } from '../tournament.model';
import { TournamentService } from '../service/tournament.service';

import { TournamentRoutingResolveService } from './tournament-routing-resolve.service';

describe('Service Tests', () => {
  describe('Tournament routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TournamentRoutingResolveService;
    let service: TournamentService;
    let resultTournament: ITournament | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TournamentRoutingResolveService);
      service = TestBed.inject(TournamentService);
      resultTournament = undefined;
    });

    describe('resolve', () => {
      it('should return ITournament returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTournament = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTournament).toEqual({ id: 123 });
      });

      it('should return new ITournament if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTournament = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTournament).toEqual(new Tournament());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTournament = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTournament).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
