import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFormat, Format } from '../format.model';
import { FormatService } from '../service/format.service';

@Injectable({ providedIn: 'root' })
export class FormatRoutingResolveService implements Resolve<IFormat> {
  constructor(protected service: FormatService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFormat> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((format: HttpResponse<Format>) => {
          if (format.body) {
            return of(format.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Format());
  }
}
