import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMainRoster, getMainRosterIdentifier } from '../main-roster.model';

export type EntityResponseType = HttpResponse<IMainRoster>;
export type EntityArrayResponseType = HttpResponse<IMainRoster[]>;

@Injectable({ providedIn: 'root' })
export class MainRosterService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/main-rosters');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(mainRoster: IMainRoster): Observable<EntityResponseType> {
    return this.http.post<IMainRoster>(this.resourceUrl, mainRoster, { observe: 'response' });
  }

  update(mainRoster: IMainRoster): Observable<EntityResponseType> {
    return this.http.put<IMainRoster>(`${this.resourceUrl}/${getMainRosterIdentifier(mainRoster) as number}`, mainRoster, {
      observe: 'response',
    });
  }

  partialUpdate(mainRoster: IMainRoster): Observable<EntityResponseType> {
    return this.http.patch<IMainRoster>(`${this.resourceUrl}/${getMainRosterIdentifier(mainRoster) as number}`, mainRoster, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMainRoster>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMainRoster[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMainRosterToCollectionIfMissing(
    mainRosterCollection: IMainRoster[],
    ...mainRostersToCheck: (IMainRoster | null | undefined)[]
  ): IMainRoster[] {
    const mainRosters: IMainRoster[] = mainRostersToCheck.filter(isPresent);
    if (mainRosters.length > 0) {
      const mainRosterCollectionIdentifiers = mainRosterCollection.map(mainRosterItem => getMainRosterIdentifier(mainRosterItem)!);
      const mainRostersToAdd = mainRosters.filter(mainRosterItem => {
        const mainRosterIdentifier = getMainRosterIdentifier(mainRosterItem);
        if (mainRosterIdentifier == null || mainRosterCollectionIdentifiers.includes(mainRosterIdentifier)) {
          return false;
        }
        mainRosterCollectionIdentifiers.push(mainRosterIdentifier);
        return true;
      });
      return [...mainRostersToAdd, ...mainRosterCollection];
    }
    return mainRosterCollection;
  }
}
