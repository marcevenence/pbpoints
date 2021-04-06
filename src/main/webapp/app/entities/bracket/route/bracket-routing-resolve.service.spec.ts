jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IBracket, Bracket } from '../bracket.model';
import { BracketService } from '../service/bracket.service';

import { BracketRoutingResolveService } from './bracket-routing-resolve.service';

describe('Service Tests', () => {
  describe('Bracket routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: BracketRoutingResolveService;
    let service: BracketService;
    let resultBracket: IBracket | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(BracketRoutingResolveService);
      service = TestBed.inject(BracketService);
      resultBracket = undefined;
    });

    describe('resolve', () => {
      it('should return IBracket returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBracket = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBracket).toEqual({ id: 123 });
      });

      it('should return new IBracket if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBracket = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultBracket).toEqual(new Bracket());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBracket = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBracket).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
