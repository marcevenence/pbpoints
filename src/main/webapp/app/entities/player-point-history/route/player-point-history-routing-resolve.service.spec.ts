import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IPlayerPointHistory, PlayerPointHistory } from '../player-point-history.model';
import { PlayerPointHistoryService } from '../service/player-point-history.service';

import { PlayerPointHistoryRoutingResolveService } from './player-point-history-routing-resolve.service';

describe('PlayerPointHistory routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PlayerPointHistoryRoutingResolveService;
  let service: PlayerPointHistoryService;
  let resultPlayerPointHistory: IPlayerPointHistory | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(PlayerPointHistoryRoutingResolveService);
    service = TestBed.inject(PlayerPointHistoryService);
    resultPlayerPointHistory = undefined;
  });

  describe('resolve', () => {
    it('should return IPlayerPointHistory returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPlayerPointHistory = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPlayerPointHistory).toEqual({ id: 123 });
    });

    it('should return new IPlayerPointHistory if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPlayerPointHistory = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPlayerPointHistory).toEqual(new PlayerPointHistory());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: (null as unknown) as PlayerPointHistory })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPlayerPointHistory = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPlayerPointHistory).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
