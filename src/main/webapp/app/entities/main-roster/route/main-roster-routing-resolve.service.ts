import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMainRoster, MainRoster } from '../main-roster.model';
import { MainRosterService } from '../service/main-roster.service';

@Injectable({ providedIn: 'root' })
export class MainRosterRoutingResolveService implements Resolve<IMainRoster> {
  constructor(protected service: MainRosterService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMainRoster> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((mainRoster: HttpResponse<MainRoster>) => {
          if (mainRoster.body) {
            return of(mainRoster.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MainRoster());
  }
}
