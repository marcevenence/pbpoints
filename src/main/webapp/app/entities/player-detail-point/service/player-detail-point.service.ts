import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlayerDetailPoint, getPlayerDetailPointIdentifier } from '../player-detail-point.model';

export type EntityResponseType = HttpResponse<IPlayerDetailPoint>;
export type EntityArrayResponseType = HttpResponse<IPlayerDetailPoint[]>;

@Injectable({ providedIn: 'root' })
export class PlayerDetailPointService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/player-detail-points');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(playerDetailPoint: IPlayerDetailPoint): Observable<EntityResponseType> {
    return this.http.post<IPlayerDetailPoint>(this.resourceUrl, playerDetailPoint, { observe: 'response' });
  }

  update(playerDetailPoint: IPlayerDetailPoint): Observable<EntityResponseType> {
    return this.http.put<IPlayerDetailPoint>(
      `${this.resourceUrl}/${getPlayerDetailPointIdentifier(playerDetailPoint) as number}`,
      playerDetailPoint,
      { observe: 'response' }
    );
  }

  partialUpdate(playerDetailPoint: IPlayerDetailPoint): Observable<EntityResponseType> {
    return this.http.patch<IPlayerDetailPoint>(
      `${this.resourceUrl}/${getPlayerDetailPointIdentifier(playerDetailPoint) as number}`,
      playerDetailPoint,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPlayerDetailPoint>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPlayerDetailPoint[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPlayerDetailPointToCollectionIfMissing(
    playerDetailPointCollection: IPlayerDetailPoint[],
    ...playerDetailPointsToCheck: (IPlayerDetailPoint | null | undefined)[]
  ): IPlayerDetailPoint[] {
    const playerDetailPoints: IPlayerDetailPoint[] = playerDetailPointsToCheck.filter(isPresent);
    if (playerDetailPoints.length > 0) {
      const playerDetailPointCollectionIdentifiers = playerDetailPointCollection.map(
        playerDetailPointItem => getPlayerDetailPointIdentifier(playerDetailPointItem)!
      );
      const playerDetailPointsToAdd = playerDetailPoints.filter(playerDetailPointItem => {
        const playerDetailPointIdentifier = getPlayerDetailPointIdentifier(playerDetailPointItem);
        if (playerDetailPointIdentifier == null || playerDetailPointCollectionIdentifiers.includes(playerDetailPointIdentifier)) {
          return false;
        }
        playerDetailPointCollectionIdentifiers.push(playerDetailPointIdentifier);
        return true;
      });
      return [...playerDetailPointsToAdd, ...playerDetailPointCollection];
    }
    return playerDetailPointCollection;
  }
}
