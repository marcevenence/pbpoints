import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IPlayerPoint } from 'app/entities/player-point/player-point.model';
import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlayer, getPlayerIdentifier } from '../player.model';

export type EntityResponseType = HttpResponse<IPlayer>;
export type EntityArrayResponseType = HttpResponse<IPlayer[]>;

@Injectable({ providedIn: 'root' })
export class PlayerService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/players');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(player: IPlayer): Observable<EntityResponseType> {
    return this.http.post<IPlayer>(this.resourceUrl, player, { observe: 'response' });
  }

  update(player: IPlayer): Observable<EntityResponseType> {
    return this.http.put<IPlayer>(`${this.resourceUrl}`, player, { observe: 'response' });
  }

  partialUpdate(player: IPlayer): Observable<EntityResponseType> {
    return this.http.patch<IPlayer>(`${this.resourceUrl}/${getPlayerIdentifier(player) as number}`, player, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPlayer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPlayer[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  validateCategory(id: number, tId: number, catId: number): Observable<HttpResponse<IPlayerPoint>> {
    return this.http.get<IPlayerPoint>(`${this.resourceUrl}/validCategory/${id}/${tId}/${catId}`, { observe: 'response' });
  }

  findOwner(id: number): Observable<HttpResponse<number>> {
    const options = createRequestOption(id);
    return this.http.get<number>(`${this.resourceUrl}/own/${id}`, { params: options, observe: 'response' });
  }

  enableUpdate(id: number): Observable<HttpResponse<number>> {
    const options = createRequestOption(id);
    return this.http.get<number>(`${this.resourceUrl}/upd/${id}`, { params: options, observe: 'response' });
  }
  addPlayerToCollectionIfMissing(playerCollection: IPlayer[], ...playersToCheck: (IPlayer | null | undefined)[]): IPlayer[] {
    const players: IPlayer[] = playersToCheck.filter(isPresent);
    if (players.length > 0) {
      const playerCollectionIdentifiers = playerCollection.map(playerItem => getPlayerIdentifier(playerItem)!);
      const playersToAdd = players.filter(playerItem => {
        const playerIdentifier = getPlayerIdentifier(playerItem);
        if (playerIdentifier == null || playerCollectionIdentifiers.includes(playerIdentifier)) {
          return false;
        }
        playerCollectionIdentifiers.push(playerIdentifier);
        return true;
      });
      return [...playersToAdd, ...playerCollection];
    }
    return playerCollection;
  }
}
