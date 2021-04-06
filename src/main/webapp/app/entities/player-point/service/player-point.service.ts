import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlayerPoint, getPlayerPointIdentifier } from '../player-point.model';

export type EntityResponseType = HttpResponse<IPlayerPoint>;
export type EntityArrayResponseType = HttpResponse<IPlayerPoint[]>;

@Injectable({ providedIn: 'root' })
export class PlayerPointService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/player-points');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(playerPoint: IPlayerPoint): Observable<EntityResponseType> {
    return this.http.post<IPlayerPoint>(this.resourceUrl, playerPoint, { observe: 'response' });
  }

  update(playerPoint: IPlayerPoint): Observable<EntityResponseType> {
    return this.http.put<IPlayerPoint>(`${this.resourceUrl}/${getPlayerPointIdentifier(playerPoint) as number}`, playerPoint, {
      observe: 'response',
    });
  }

  partialUpdate(playerPoint: IPlayerPoint): Observable<EntityResponseType> {
    return this.http.patch<IPlayerPoint>(`${this.resourceUrl}/${getPlayerPointIdentifier(playerPoint) as number}`, playerPoint, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPlayerPoint>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPlayerPoint[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPlayerPointToCollectionIfMissing(
    playerPointCollection: IPlayerPoint[],
    ...playerPointsToCheck: (IPlayerPoint | null | undefined)[]
  ): IPlayerPoint[] {
    const playerPoints: IPlayerPoint[] = playerPointsToCheck.filter(isPresent);
    if (playerPoints.length > 0) {
      const playerPointCollectionIdentifiers = playerPointCollection.map(playerPointItem => getPlayerPointIdentifier(playerPointItem)!);
      const playerPointsToAdd = playerPoints.filter(playerPointItem => {
        const playerPointIdentifier = getPlayerPointIdentifier(playerPointItem);
        if (playerPointIdentifier == null || playerPointCollectionIdentifiers.includes(playerPointIdentifier)) {
          return false;
        }
        playerPointCollectionIdentifiers.push(playerPointIdentifier);
        return true;
      });
      return [...playerPointsToAdd, ...playerPointCollection];
    }
    return playerPointCollection;
  }
}
