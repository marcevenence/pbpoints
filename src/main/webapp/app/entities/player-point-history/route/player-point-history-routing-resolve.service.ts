import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPlayerPointHistory, PlayerPointHistory } from '../player-point-history.model';
import { PlayerPointHistoryService } from '../service/player-point-history.service';

@Injectable({ providedIn: 'root' })
export class PlayerPointHistoryRoutingResolveService implements Resolve<IPlayerPointHistory> {
  constructor(protected service: PlayerPointHistoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPlayerPointHistory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((playerPointHistory: HttpResponse<PlayerPointHistory>) => {
          if (playerPointHistory.body) {
            return of(playerPointHistory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PlayerPointHistory());
  }
}
