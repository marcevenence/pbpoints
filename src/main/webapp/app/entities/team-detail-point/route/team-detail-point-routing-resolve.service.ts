import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITeamDetailPoint, TeamDetailPoint } from '../team-detail-point.model';
import { TeamDetailPointService } from '../service/team-detail-point.service';

@Injectable({ providedIn: 'root' })
export class TeamDetailPointRoutingResolveService implements Resolve<ITeamDetailPoint> {
  constructor(protected service: TeamDetailPointService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITeamDetailPoint> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((teamDetailPoint: HttpResponse<TeamDetailPoint>) => {
          if (teamDetailPoint.body) {
            return of(teamDetailPoint.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TeamDetailPoint());
  }
}
