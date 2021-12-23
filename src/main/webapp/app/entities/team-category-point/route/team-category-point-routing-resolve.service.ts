import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITeamCategoryPoint, TeamCategoryPoint } from '../team-category-point.model';
import { TeamCategoryPointService } from '../service/team-category-point.service';

@Injectable({ providedIn: 'root' })
export class TeamCategoryPointRoutingResolveService implements Resolve<ITeamCategoryPoint> {
  constructor(protected service: TeamCategoryPointService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITeamCategoryPoint> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((teamCategoryPoint: HttpResponse<TeamCategoryPoint>) => {
          if (teamCategoryPoint.body) {
            return of(teamCategoryPoint.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TeamCategoryPoint());
  }
}
