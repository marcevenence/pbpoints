import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFormula, Formula } from '../formula.model';
import { FormulaService } from '../service/formula.service';

@Injectable({ providedIn: 'root' })
export class FormulaRoutingResolveService implements Resolve<IFormula> {
  constructor(protected service: FormulaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFormula> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((formula: HttpResponse<Formula>) => {
          if (formula.body) {
            return of(formula.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Formula());
  }
}
