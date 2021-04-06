import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBracket, Bracket } from '../bracket.model';
import { BracketService } from '../service/bracket.service';

@Injectable({ providedIn: 'root' })
export class BracketRoutingResolveService implements Resolve<IBracket> {
  constructor(protected service: BracketService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBracket> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((bracket: HttpResponse<Bracket>) => {
          if (bracket.body) {
            return of(bracket.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Bracket());
  }
}
