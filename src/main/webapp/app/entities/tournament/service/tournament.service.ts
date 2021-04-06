import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITournament, getTournamentIdentifier } from '../tournament.model';

export type EntityResponseType = HttpResponse<ITournament>;
export type EntityArrayResponseType = HttpResponse<ITournament[]>;

@Injectable({ providedIn: 'root' })
export class TournamentService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/tournaments');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(tournament: ITournament): Observable<EntityResponseType> {
    return this.http.post<ITournament>(this.resourceUrl, tournament, { observe: 'response' });
  }

  update(tournament: ITournament): Observable<EntityResponseType> {
    return this.http.put<ITournament>(`${this.resourceUrl}/${getTournamentIdentifier(tournament) as number}`, tournament, {
      observe: 'response',
    });
  }

  partialUpdate(tournament: ITournament): Observable<EntityResponseType> {
    return this.http.patch<ITournament>(`${this.resourceUrl}/${getTournamentIdentifier(tournament) as number}`, tournament, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITournament>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITournament[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTournamentToCollectionIfMissing(
    tournamentCollection: ITournament[],
    ...tournamentsToCheck: (ITournament | null | undefined)[]
  ): ITournament[] {
    const tournaments: ITournament[] = tournamentsToCheck.filter(isPresent);
    if (tournaments.length > 0) {
      const tournamentCollectionIdentifiers = tournamentCollection.map(tournamentItem => getTournamentIdentifier(tournamentItem)!);
      const tournamentsToAdd = tournaments.filter(tournamentItem => {
        const tournamentIdentifier = getTournamentIdentifier(tournamentItem);
        if (tournamentIdentifier == null || tournamentCollectionIdentifiers.includes(tournamentIdentifier)) {
          return false;
        }
        tournamentCollectionIdentifiers.push(tournamentIdentifier);
        return true;
      });
      return [...tournamentsToAdd, ...tournamentCollection];
    }
    return tournamentCollection;
  }
}
