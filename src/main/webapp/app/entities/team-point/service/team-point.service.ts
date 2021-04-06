import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITeamPoint, getTeamPointIdentifier } from '../team-point.model';

export type EntityResponseType = HttpResponse<ITeamPoint>;
export type EntityArrayResponseType = HttpResponse<ITeamPoint[]>;

@Injectable({ providedIn: 'root' })
export class TeamPointService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/team-points');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(teamPoint: ITeamPoint): Observable<EntityResponseType> {
    return this.http.post<ITeamPoint>(this.resourceUrl, teamPoint, { observe: 'response' });
  }

  update(teamPoint: ITeamPoint): Observable<EntityResponseType> {
    return this.http.put<ITeamPoint>(`${this.resourceUrl}/${getTeamPointIdentifier(teamPoint) as number}`, teamPoint, {
      observe: 'response',
    });
  }

  partialUpdate(teamPoint: ITeamPoint): Observable<EntityResponseType> {
    return this.http.patch<ITeamPoint>(`${this.resourceUrl}/${getTeamPointIdentifier(teamPoint) as number}`, teamPoint, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITeamPoint>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITeamPoint[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTeamPointToCollectionIfMissing(
    teamPointCollection: ITeamPoint[],
    ...teamPointsToCheck: (ITeamPoint | null | undefined)[]
  ): ITeamPoint[] {
    const teamPoints: ITeamPoint[] = teamPointsToCheck.filter(isPresent);
    if (teamPoints.length > 0) {
      const teamPointCollectionIdentifiers = teamPointCollection.map(teamPointItem => getTeamPointIdentifier(teamPointItem)!);
      const teamPointsToAdd = teamPoints.filter(teamPointItem => {
        const teamPointIdentifier = getTeamPointIdentifier(teamPointItem);
        if (teamPointIdentifier == null || teamPointCollectionIdentifiers.includes(teamPointIdentifier)) {
          return false;
        }
        teamPointCollectionIdentifiers.push(teamPointIdentifier);
        return true;
      });
      return [...teamPointsToAdd, ...teamPointCollection];
    }
    return teamPointCollection;
  }
}
