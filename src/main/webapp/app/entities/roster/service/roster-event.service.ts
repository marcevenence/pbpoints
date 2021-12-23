import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IRosterEvent } from '../roster-event.model';

export type EntityResponseType = HttpResponse<IRosterEvent>;
export type EntityArrayResponseType = HttpResponse<IRosterEvent[]>;

@Injectable({ providedIn: 'root' })
export class RosterEventService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/rosterEvent');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  query(id: number): Observable<EntityArrayResponseType> {
    return this.http.get<IRosterEvent[]>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
