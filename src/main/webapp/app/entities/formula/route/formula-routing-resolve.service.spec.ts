jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFormula, Formula } from '../formula.model';
import { FormulaService } from '../service/formula.service';

import { FormulaRoutingResolveService } from './formula-routing-resolve.service';

describe('Service Tests', () => {
  describe('Formula routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: FormulaRoutingResolveService;
    let service: FormulaService;
    let resultFormula: IFormula | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(FormulaRoutingResolveService);
      service = TestBed.inject(FormulaService);
      resultFormula = undefined;
    });

    describe('resolve', () => {
      it('should return IFormula returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFormula = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFormula).toEqual({ id: 123 });
      });

      it('should return new IFormula if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFormula = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultFormula).toEqual(new Formula());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFormula = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFormula).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
