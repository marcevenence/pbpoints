import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITeamCategoryPoint, getTeamCategoryPointIdentifier } from '../team-category-point.model';

export type EntityResponseType = HttpResponse<ITeamCategoryPoint>;
export type EntityArrayResponseType = HttpResponse<ITeamCategoryPoint[]>;

@Injectable({ providedIn: 'root' })
export class TeamCategoryPointService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/team-category-points');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(teamCategoryPoint: ITeamCategoryPoint): Observable<EntityResponseType> {
    return this.http.post<ITeamCategoryPoint>(this.resourceUrl, teamCategoryPoint, { observe: 'response' });
  }

  update(teamCategoryPoint: ITeamCategoryPoint): Observable<EntityResponseType> {
    return this.http.put<ITeamCategoryPoint>(
      `${this.resourceUrl}/${getTeamCategoryPointIdentifier(teamCategoryPoint) as number}`,
      teamCategoryPoint,
      { observe: 'response' }
    );
  }

  partialUpdate(teamCategoryPoint: ITeamCategoryPoint): Observable<EntityResponseType> {
    return this.http.patch<ITeamCategoryPoint>(
      `${this.resourceUrl}/${getTeamCategoryPointIdentifier(teamCategoryPoint) as number}`,
      teamCategoryPoint,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITeamCategoryPoint>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITeamCategoryPoint[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTeamCategoryPointToCollectionIfMissing(
    teamCategoryPointCollection: ITeamCategoryPoint[],
    ...teamCategoryPointsToCheck: (ITeamCategoryPoint | null | undefined)[]
  ): ITeamCategoryPoint[] {
    const teamCategoryPoints: ITeamCategoryPoint[] = teamCategoryPointsToCheck.filter(isPresent);
    if (teamCategoryPoints.length > 0) {
      const teamCategoryPointCollectionIdentifiers = teamCategoryPointCollection.map(
        teamCategoryPointItem => getTeamCategoryPointIdentifier(teamCategoryPointItem)!
      );
      const teamCategoryPointsToAdd = teamCategoryPoints.filter(teamCategoryPointItem => {
        const teamCategoryPointIdentifier = getTeamCategoryPointIdentifier(teamCategoryPointItem);
        if (teamCategoryPointIdentifier == null || teamCategoryPointCollectionIdentifiers.includes(teamCategoryPointIdentifier)) {
          return false;
        }
        teamCategoryPointCollectionIdentifiers.push(teamCategoryPointIdentifier);
        return true;
      });
      return [...teamCategoryPointsToAdd, ...teamCategoryPointCollection];
    }
    return teamCategoryPointCollection;
  }
}
