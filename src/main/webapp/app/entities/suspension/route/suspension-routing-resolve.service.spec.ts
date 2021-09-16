jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISuspension, Suspension } from '../suspension.model';
import { SuspensionService } from '../service/suspension.service';

import { SuspensionRoutingResolveService } from './suspension-routing-resolve.service';

describe('Service Tests', () => {
  describe('Suspension routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SuspensionRoutingResolveService;
    let service: SuspensionService;
    let resultSuspension: ISuspension | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SuspensionRoutingResolveService);
      service = TestBed.inject(SuspensionService);
      resultSuspension = undefined;
    });

    describe('resolve', () => {
      it('should return ISuspension returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSuspension = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSuspension).toEqual({ id: 123 });
      });

      it('should return new ISuspension if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSuspension = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSuspension).toEqual(new Suspension());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSuspension = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSuspension).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
