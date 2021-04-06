jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IGame, Game } from '../game.model';
import { GameService } from '../service/game.service';

import { GameRoutingResolveService } from './game-routing-resolve.service';

describe('Service Tests', () => {
  describe('Game routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: GameRoutingResolveService;
    let service: GameService;
    let resultGame: IGame | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(GameRoutingResolveService);
      service = TestBed.inject(GameService);
      resultGame = undefined;
    });

    describe('resolve', () => {
      it('should return IGame returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultGame = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultGame).toEqual({ id: 123 });
      });

      it('should return new IGame if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultGame = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultGame).toEqual(new Game());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultGame = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultGame).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
