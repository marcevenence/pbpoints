import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocType, DocType } from '../doc-type.model';
import { DocTypeService } from '../service/doc-type.service';

@Injectable({ providedIn: 'root' })
export class DocTypeRoutingResolveService implements Resolve<IDocType> {
  constructor(protected service: DocTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDocType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((docType: HttpResponse<DocType>) => {
          if (docType.body) {
            return of(docType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DocType());
  }
}
