import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITeamDetailPoint, getTeamDetailPointIdentifier } from '../team-detail-point.model';

export type EntityResponseType = HttpResponse<ITeamDetailPoint>;
export type EntityArrayResponseType = HttpResponse<ITeamDetailPoint[]>;

@Injectable({ providedIn: 'root' })
export class TeamDetailPointService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/team-detail-points');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(teamDetailPoint: ITeamDetailPoint): Observable<EntityResponseType> {
    return this.http.post<ITeamDetailPoint>(this.resourceUrl, teamDetailPoint, { observe: 'response' });
  }

  update(teamDetailPoint: ITeamDetailPoint): Observable<EntityResponseType> {
    return this.http.put<ITeamDetailPoint>(
      `${this.resourceUrl}/${getTeamDetailPointIdentifier(teamDetailPoint) as number}`,
      teamDetailPoint,
      { observe: 'response' }
    );
  }

  partialUpdate(teamDetailPoint: ITeamDetailPoint): Observable<EntityResponseType> {
    return this.http.patch<ITeamDetailPoint>(
      `${this.resourceUrl}/${getTeamDetailPointIdentifier(teamDetailPoint) as number}`,
      teamDetailPoint,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITeamDetailPoint>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITeamDetailPoint[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTeamDetailPointToCollectionIfMissing(
    teamDetailPointCollection: ITeamDetailPoint[],
    ...teamDetailPointsToCheck: (ITeamDetailPoint | null | undefined)[]
  ): ITeamDetailPoint[] {
    const teamDetailPoints: ITeamDetailPoint[] = teamDetailPointsToCheck.filter(isPresent);
    if (teamDetailPoints.length > 0) {
      const teamDetailPointCollectionIdentifiers = teamDetailPointCollection.map(
        teamDetailPointItem => getTeamDetailPointIdentifier(teamDetailPointItem)!
      );
      const teamDetailPointsToAdd = teamDetailPoints.filter(teamDetailPointItem => {
        const teamDetailPointIdentifier = getTeamDetailPointIdentifier(teamDetailPointItem);
        if (teamDetailPointIdentifier == null || teamDetailPointCollectionIdentifiers.includes(teamDetailPointIdentifier)) {
          return false;
        }
        teamDetailPointCollectionIdentifiers.push(teamDetailPointIdentifier);
        return true;
      });
      return [...teamDetailPointsToAdd, ...teamDetailPointCollection];
    }
    return teamDetailPointCollection;
  }
}
