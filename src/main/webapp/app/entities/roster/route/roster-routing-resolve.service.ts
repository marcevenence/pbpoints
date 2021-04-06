import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRoster, Roster } from '../roster.model';
import { RosterService } from '../service/roster.service';

@Injectable({ providedIn: 'root' })
export class RosterRoutingResolveService implements Resolve<IRoster> {
  constructor(protected service: RosterService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRoster> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((roster: HttpResponse<Roster>) => {
          if (roster.body) {
            return of(roster.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Roster());
  }
}
