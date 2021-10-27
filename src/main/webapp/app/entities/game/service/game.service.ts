import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGame, getGameIdentifier } from '../game.model';

export type EntityResponseType = HttpResponse<IGame>;
export type EntityArrayResponseType = HttpResponse<IGame[]>;

@Injectable({ providedIn: 'root' })
export class GameService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/games');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(game: IGame): Observable<EntityResponseType> {
    return this.http.post<IGame>(this.resourceUrl, game, { observe: 'response' });
  }

  update(game: IGame): Observable<EntityResponseType> {
    return this.http.put<IGame>(`${this.resourceUrl}/${getGameIdentifier(game) as number}`, game, { observe: 'response' });
  }

  partialUpdate(game: IGame): Observable<EntityResponseType> {
    return this.http.patch<IGame>(`${this.resourceUrl}/${getGameIdentifier(game) as number}`, game, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGame>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGame[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addGameToCollectionIfMissing(gameCollection: IGame[], ...gamesToCheck: (IGame | null | undefined)[]): IGame[] {
    const games: IGame[] = gamesToCheck.filter(isPresent);
    if (games.length > 0) {
      const gameCollectionIdentifiers = gameCollection.map(gameItem => getGameIdentifier(gameItem)!);
      const gamesToAdd = games.filter(gameItem => {
        const gameIdentifier = getGameIdentifier(gameItem);
        if (gameIdentifier == null || gameCollectionIdentifiers.includes(gameIdentifier)) {
          return false;
        }
        gameCollectionIdentifiers.push(gameIdentifier);
        return true;
      });
      return [...gamesToAdd, ...gameCollection];
    }
    return gameCollection;
  }
}
