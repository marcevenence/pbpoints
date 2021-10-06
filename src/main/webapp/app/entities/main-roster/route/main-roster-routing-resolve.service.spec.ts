jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMainRoster, MainRoster } from '../main-roster.model';
import { MainRosterService } from '../service/main-roster.service';

import { MainRosterRoutingResolveService } from './main-roster-routing-resolve.service';

describe('Service Tests', () => {
  describe('MainRoster routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: MainRosterRoutingResolveService;
    let service: MainRosterService;
    let resultMainRoster: IMainRoster | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(MainRosterRoutingResolveService);
      service = TestBed.inject(MainRosterService);
      resultMainRoster = undefined;
    });

    describe('resolve', () => {
      it('should return IMainRoster returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMainRoster = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMainRoster).toEqual({ id: 123 });
      });

      it('should return new IMainRoster if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMainRoster = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultMainRoster).toEqual(new MainRoster());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMainRoster = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMainRoster).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
