import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISuspension, Suspension } from '../suspension.model';
import { SuspensionService } from '../service/suspension.service';

@Injectable({ providedIn: 'root' })
export class SuspensionRoutingResolveService implements Resolve<ISuspension> {
  constructor(protected service: SuspensionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISuspension> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((suspension: HttpResponse<Suspension>) => {
          if (suspension.body) {
            return of(suspension.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Suspension());
  }
}
