import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPlayerPoint, PlayerPoint } from '../player-point.model';
import { PlayerPointService } from '../service/player-point.service';

@Injectable({ providedIn: 'root' })
export class PlayerPointRoutingResolveService implements Resolve<IPlayerPoint> {
  constructor(protected service: PlayerPointService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPlayerPoint> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((playerPoint: HttpResponse<PlayerPoint>) => {
          if (playerPoint.body) {
            return of(playerPoint.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PlayerPoint());
  }
}
