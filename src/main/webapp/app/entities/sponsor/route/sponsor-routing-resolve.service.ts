import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISponsor, Sponsor } from '../sponsor.model';
import { SponsorService } from '../service/sponsor.service';

@Injectable({ providedIn: 'root' })
export class SponsorRoutingResolveService implements Resolve<ISponsor> {
  constructor(protected service: SponsorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISponsor> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sponsor: HttpResponse<Sponsor>) => {
          if (sponsor.body) {
            return of(sponsor.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Sponsor());
  }
}
