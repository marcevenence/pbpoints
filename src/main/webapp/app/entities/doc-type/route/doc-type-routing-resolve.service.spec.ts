jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDocType, DocType } from '../doc-type.model';
import { DocTypeService } from '../service/doc-type.service';

import { DocTypeRoutingResolveService } from './doc-type-routing-resolve.service';

describe('Service Tests', () => {
  describe('DocType routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DocTypeRoutingResolveService;
    let service: DocTypeService;
    let resultDocType: IDocType | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DocTypeRoutingResolveService);
      service = TestBed.inject(DocTypeService);
      resultDocType = undefined;
    });

    describe('resolve', () => {
      it('should return IDocType returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDocType = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDocType).toEqual({ id: 123 });
      });

      it('should return new IDocType if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDocType = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDocType).toEqual(new DocType());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDocType = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDocType).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
