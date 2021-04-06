import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEventCategory, EventCategory } from '../event-category.model';
import { EventCategoryService } from '../service/event-category.service';

@Injectable({ providedIn: 'root' })
export class EventCategoryRoutingResolveService implements Resolve<IEventCategory> {
  constructor(protected service: EventCategoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEventCategory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((eventCategory: HttpResponse<EventCategory>) => {
          if (eventCategory.body) {
            return of(eventCategory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EventCategory());
  }
}
