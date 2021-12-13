import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
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
    const copy = this.convertDateFromClient(tournament);
    return this.http
      .post<ITournament>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(tournament: ITournament): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tournament);
    return this.http
      .put<ITournament>(`${this.resourceUrl}/${getTournamentIdentifier(tournament) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(tournament: ITournament): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tournament);
    return this.http
      .patch<ITournament>(`${this.resourceUrl}/${getTournamentIdentifier(tournament) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITournament>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITournament[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
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

  protected convertDateFromClient(tournament: ITournament): ITournament {
    return Object.assign({}, tournament, {
      startSeason: tournament.startSeason?.isValid() ? tournament.startSeason.format(DATE_FORMAT) : undefined,
      endSeason: tournament.endSeason?.isValid() ? tournament.endSeason.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startSeason = res.body.startSeason ? dayjs(res.body.startSeason) : undefined;
      res.body.endSeason = res.body.endSeason ? dayjs(res.body.endSeason) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((tournament: ITournament) => {
        tournament.startSeason = tournament.startSeason ? dayjs(tournament.startSeason) : undefined;
        tournament.endSeason = tournament.endSeason ? dayjs(tournament.endSeason) : undefined;
      });
    }
    return res;
  }
}
