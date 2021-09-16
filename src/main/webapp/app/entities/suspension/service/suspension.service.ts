import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISuspension, getSuspensionIdentifier } from '../suspension.model';

export type EntityResponseType = HttpResponse<ISuspension>;
export type EntityArrayResponseType = HttpResponse<ISuspension[]>;

@Injectable({ providedIn: 'root' })
export class SuspensionService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/suspensions');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(suspension: ISuspension): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(suspension);
    alert(copy.user?.id!.toString());
    return this.http
      .post<ISuspension>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(suspension: ISuspension): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(suspension);
    return this.http
      .put<ISuspension>(`${this.resourceUrl}/${getSuspensionIdentifier(suspension) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(suspension: ISuspension): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(suspension);
    return this.http
      .patch<ISuspension>(`${this.resourceUrl}/${getSuspensionIdentifier(suspension) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISuspension>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISuspension[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSuspensionToCollectionIfMissing(
    suspensionCollection: ISuspension[],
    ...suspensionsToCheck: (ISuspension | null | undefined)[]
  ): ISuspension[] {
    const suspensions: ISuspension[] = suspensionsToCheck.filter(isPresent);
    if (suspensions.length > 0) {
      const suspensionCollectionIdentifiers = suspensionCollection.map(suspensionItem => getSuspensionIdentifier(suspensionItem)!);
      const suspensionsToAdd = suspensions.filter(suspensionItem => {
        const suspensionIdentifier = getSuspensionIdentifier(suspensionItem);
        if (suspensionIdentifier == null || suspensionCollectionIdentifiers.includes(suspensionIdentifier)) {
          return false;
        }
        suspensionCollectionIdentifiers.push(suspensionIdentifier);
        return true;
      });
      return [...suspensionsToAdd, ...suspensionCollection];
    }
    return suspensionCollection;
  }

  protected convertDateFromClient(suspension: ISuspension): ISuspension {
    return Object.assign({}, suspension, {
      startDate: suspension.startDate?.isValid() ? suspension.startDate.format(DATE_FORMAT) : undefined,
      endDate: suspension.endDate?.isValid() ? suspension.endDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? dayjs(res.body.startDate) : undefined;
      res.body.endDate = res.body.endDate ? dayjs(res.body.endDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((suspension: ISuspension) => {
        suspension.startDate = suspension.startDate ? dayjs(suspension.startDate) : undefined;
        suspension.endDate = suspension.endDate ? dayjs(suspension.endDate) : undefined;
      });
    }
    return res;
  }
}
