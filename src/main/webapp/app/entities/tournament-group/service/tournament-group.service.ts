import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITournamentGroup, getTournamentGroupIdentifier } from '../tournament-group.model';

export type EntityResponseType = HttpResponse<ITournamentGroup>;
export type EntityArrayResponseType = HttpResponse<ITournamentGroup[]>;

@Injectable({ providedIn: 'root' })
export class TournamentGroupService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/tournament-groups');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(tournamentGroup: ITournamentGroup): Observable<EntityResponseType> {
    return this.http.post<ITournamentGroup>(this.resourceUrl, tournamentGroup, { observe: 'response' });
  }

  update(tournamentGroup: ITournamentGroup): Observable<EntityResponseType> {
    return this.http.put<ITournamentGroup>(
      `${this.resourceUrl}/${getTournamentGroupIdentifier(tournamentGroup) as number}`,
      tournamentGroup,
      { observe: 'response' }
    );
  }

  partialUpdate(tournamentGroup: ITournamentGroup): Observable<EntityResponseType> {
    return this.http.patch<ITournamentGroup>(
      `${this.resourceUrl}/${getTournamentGroupIdentifier(tournamentGroup) as number}`,
      tournamentGroup,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITournamentGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITournamentGroup[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTournamentGroupToCollectionIfMissing(
    tournamentGroupCollection: ITournamentGroup[],
    ...tournamentGroupsToCheck: (ITournamentGroup | null | undefined)[]
  ): ITournamentGroup[] {
    const tournamentGroups: ITournamentGroup[] = tournamentGroupsToCheck.filter(isPresent);
    if (tournamentGroups.length > 0) {
      const tournamentGroupCollectionIdentifiers = tournamentGroupCollection.map(
        tournamentGroupItem => getTournamentGroupIdentifier(tournamentGroupItem)!
      );
      const tournamentGroupsToAdd = tournamentGroups.filter(tournamentGroupItem => {
        const tournamentGroupIdentifier = getTournamentGroupIdentifier(tournamentGroupItem);
        if (tournamentGroupIdentifier == null || tournamentGroupCollectionIdentifiers.includes(tournamentGroupIdentifier)) {
          return false;
        }
        tournamentGroupCollectionIdentifiers.push(tournamentGroupIdentifier);
        return true;
      });
      return [...tournamentGroupsToAdd, ...tournamentGroupCollection];
    }
    return tournamentGroupCollection;
  }
}
