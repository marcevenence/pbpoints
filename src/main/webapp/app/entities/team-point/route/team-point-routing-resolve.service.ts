import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITeamPoint, TeamPoint } from '../team-point.model';
import { TeamPointService } from '../service/team-point.service';

@Injectable({ providedIn: 'root' })
export class TeamPointRoutingResolveService implements Resolve<ITeamPoint> {
  constructor(protected service: TeamPointService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITeamPoint> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((teamPoint: HttpResponse<TeamPoint>) => {
          if (teamPoint.body) {
            return of(teamPoint.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TeamPoint());
  }
}
