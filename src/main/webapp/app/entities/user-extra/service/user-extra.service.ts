import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserExtra, getUserExtraIdentifier } from '../user-extra.model';

export type EntityResponseType = HttpResponse<IUserExtra>;
export type EntityArrayResponseType = HttpResponse<IUserExtra[]>;

@Injectable({ providedIn: 'root' })
export class UserExtraService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/user-extras');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(userExtra: IUserExtra): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userExtra);
    return this.http
      .post<IUserExtra>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(userExtra: IUserExtra): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userExtra);
    return this.http
      .put<IUserExtra>(`${this.resourceUrl}/${getUserExtraIdentifier(userExtra) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(userExtra: IUserExtra): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userExtra);
    return this.http
      .patch<IUserExtra>(`${this.resourceUrl}/${getUserExtraIdentifier(userExtra) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IUserExtra>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IUserExtra[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUserExtraToCollectionIfMissing(
    userExtraCollection: IUserExtra[],
    ...userExtrasToCheck: (IUserExtra | null | undefined)[]
  ): IUserExtra[] {
    const userExtras: IUserExtra[] = userExtrasToCheck.filter(isPresent);
    if (userExtras.length > 0) {
      const userExtraCollectionIdentifiers = userExtraCollection.map(userExtraItem => getUserExtraIdentifier(userExtraItem)!);
      const userExtrasToAdd = userExtras.filter(userExtraItem => {
        const userExtraIdentifier = getUserExtraIdentifier(userExtraItem);
        if (userExtraIdentifier == null || userExtraCollectionIdentifiers.includes(userExtraIdentifier)) {
          return false;
        }
        userExtraCollectionIdentifiers.push(userExtraIdentifier);
        return true;
      });
      return [...userExtrasToAdd, ...userExtraCollection];
    }
    return userExtraCollection;
  }

  protected convertDateFromClient(userExtra: IUserExtra): IUserExtra {
    return Object.assign({}, userExtra, {
      bornDate: userExtra.bornDate?.isValid() ? userExtra.bornDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.bornDate = res.body.bornDate ? dayjs(res.body.bornDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((userExtra: IUserExtra) => {
        userExtra.bornDate = userExtra.bornDate ? dayjs(userExtra.bornDate) : undefined;
      });
    }
    return res;
  }
}
