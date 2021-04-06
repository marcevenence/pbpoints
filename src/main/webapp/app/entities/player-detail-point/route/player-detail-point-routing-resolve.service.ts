import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPlayerDetailPoint, PlayerDetailPoint } from '../player-detail-point.model';
import { PlayerDetailPointService } from '../service/player-detail-point.service';

@Injectable({ providedIn: 'root' })
export class PlayerDetailPointRoutingResolveService implements Resolve<IPlayerDetailPoint> {
  constructor(protected service: PlayerDetailPointService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPlayerDetailPoint> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((playerDetailPoint: HttpResponse<PlayerDetailPoint>) => {
          if (playerDetailPoint.body) {
            return of(playerDetailPoint.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PlayerDetailPoint());
  }
}
