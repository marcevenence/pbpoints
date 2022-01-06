import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRoster, getRosterIdentifier } from '../roster.model';
import { IRosterSubs } from '../roster-subs.model';
import { IPlayer } from 'app/entities/player/player.model';
import { IEventCategory } from 'app/entities/event-category/event-category.model';
import { ITeam } from 'app/entities/team/team.model';

export type EntityResponseType = HttpResponse<IRoster>;
export type EntityArrayResponseType = HttpResponse<IRoster[]>;

@Injectable({ providedIn: 'root' })
export class RosterService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/rosters');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(roster: IRoster): Observable<EntityResponseType> {
    return this.http.post<IRoster>(this.resourceUrl, roster, { observe: 'response' });
  }

  createWithPlayers(players: IPlayer[], team: ITeam, eventCategory: IEventCategory): Observable<EntityResponseType> {
    const data = { players, team, eventCategory };
    return this.http.post<IRoster>(`${this.resourceUrl}/players`, data, { observe: 'response' });
  }

  update(roster: IRoster): Observable<EntityResponseType> {
    return this.http.put<IRoster>(`${this.resourceUrl}`, roster, { observe: 'response' });
  }

  updateWithPlayers(players: IPlayer[], id: number): Observable<EntityResponseType> {
    /* const data = { players };*/
    return this.http.put<IRoster>(`${this.resourceUrl}/players/${id}`, players, { observe: 'response' });
  }

  partialUpdate(roster: IRoster): Observable<EntityResponseType> {
    return this.http.patch<IRoster>(`${this.resourceUrl}/${getRosterIdentifier(roster) as number}`, roster, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRoster>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRoster[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  queryOne(req?: any): Observable<EntityResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRoster>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  checkinRosters(id: number, profile: string): Observable<HttpResponse<IPlayer>> {
    return this.http.get<IRoster>(`${this.resourceUrl}/check/${id}/${profile}`, { observe: 'response' });
  }

  count(req?: any): Observable<HttpResponse<number>> {
    const options = createRequestOption(req);
    return this.http.get<number>(this.resourceUrl, { params: options, observe: 'response' });
  }

  validatePlayer(rosterSubs: IRosterSubs): Observable<EntityResponseType> {
    return this.http.put<IRosterSubs>(`${this.resourceUrl}/validatePlayer`, rosterSubs, { observe: 'response' });
  }

  addRosterToCollectionIfMissing(rosterCollection: IRoster[], ...rostersToCheck: (IRoster | null | undefined)[]): IRoster[] {
    const rosters: IRoster[] = rostersToCheck.filter(isPresent);
    if (rosters.length > 0) {
      const rosterCollectionIdentifiers = rosterCollection.map(rosterItem => getRosterIdentifier(rosterItem)!);
      const rostersToAdd = rosters.filter(rosterItem => {
        const rosterIdentifier = getRosterIdentifier(rosterItem);
        if (rosterIdentifier == null || rosterCollectionIdentifiers.includes(rosterIdentifier)) {
          return false;
        }
        rosterCollectionIdentifiers.push(rosterIdentifier);
        return true;
      });
      return [...rostersToAdd, ...rosterCollection];
    }
    return rosterCollection;
  }
}
