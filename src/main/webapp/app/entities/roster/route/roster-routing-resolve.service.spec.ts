jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRoster, Roster } from '../roster.model';
import { RosterService } from '../service/roster.service';

import { RosterRoutingResolveService } from './roster-routing-resolve.service';

describe('Service Tests', () => {
  describe('Roster routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: RosterRoutingResolveService;
    let service: RosterService;
    let resultRoster: IRoster | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(RosterRoutingResolveService);
      service = TestBed.inject(RosterService);
      resultRoster = undefined;
    });

    describe('resolve', () => {
      it('should return IRoster returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRoster = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultRoster).toEqual({ id: 123 });
      });

      it('should return new IRoster if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRoster = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultRoster).toEqual(new Roster());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRoster = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultRoster).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
