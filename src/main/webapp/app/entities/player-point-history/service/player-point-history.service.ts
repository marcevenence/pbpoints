import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlayerPointHistory, getPlayerPointHistoryIdentifier } from '../player-point-history.model';

export type EntityResponseType = HttpResponse<IPlayerPointHistory>;
export type EntityArrayResponseType = HttpResponse<IPlayerPointHistory[]>;

@Injectable({ providedIn: 'root' })
export class PlayerPointHistoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/player-point-histories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(playerPointHistory: IPlayerPointHistory): Observable<EntityResponseType> {
    return this.http.post<IPlayerPointHistory>(this.resourceUrl, playerPointHistory, { observe: 'response' });
  }

  update(playerPointHistory: IPlayerPointHistory): Observable<EntityResponseType> {
    return this.http.put<IPlayerPointHistory>(
      `${this.resourceUrl}/${getPlayerPointHistoryIdentifier(playerPointHistory) as number}`,
      playerPointHistory,
      { observe: 'response' }
    );
  }

  partialUpdate(playerPointHistory: IPlayerPointHistory): Observable<EntityResponseType> {
    return this.http.patch<IPlayerPointHistory>(
      `${this.resourceUrl}/${getPlayerPointHistoryIdentifier(playerPointHistory) as number}`,
      playerPointHistory,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPlayerPointHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPlayerPointHistory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPlayerPointHistoryToCollectionIfMissing(
    playerPointHistoryCollection: IPlayerPointHistory[],
    ...playerPointHistoriesToCheck: (IPlayerPointHistory | null | undefined)[]
  ): IPlayerPointHistory[] {
    const playerPointHistories: IPlayerPointHistory[] = playerPointHistoriesToCheck.filter(isPresent);
    if (playerPointHistories.length > 0) {
      const playerPointHistoryCollectionIdentifiers = playerPointHistoryCollection.map(
        playerPointHistoryItem => getPlayerPointHistoryIdentifier(playerPointHistoryItem)!
      );
      const playerPointHistoriesToAdd = playerPointHistories.filter(playerPointHistoryItem => {
        const playerPointHistoryIdentifier = getPlayerPointHistoryIdentifier(playerPointHistoryItem);
        if (playerPointHistoryIdentifier == null || playerPointHistoryCollectionIdentifiers.includes(playerPointHistoryIdentifier)) {
          return false;
        }
        playerPointHistoryCollectionIdentifiers.push(playerPointHistoryIdentifier);
        return true;
      });
      return [...playerPointHistoriesToAdd, ...playerPointHistoryCollection];
    }
    return playerPointHistoryCollection;
  }
}
