import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEventCategory, getEventCategoryIdentifier } from '../event-category.model';

export type EntityResponseType = HttpResponse<IEventCategory>;
export type EntityArrayResponseType = HttpResponse<IEventCategory[]>;

@Injectable({ providedIn: 'root' })
export class EventCategoryService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/event-categories');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(eventCategory: IEventCategory): Observable<EntityResponseType> {
    return this.http.post<IEventCategory>(this.resourceUrl, eventCategory, { observe: 'response' });
  }

  update(eventCategory: IEventCategory): Observable<EntityResponseType> {
    return this.http.put<IEventCategory>(`${this.resourceUrl}`, eventCategory, {
      observe: 'response',
    });
  }

  partialUpdate(eventCategory: IEventCategory): Observable<EntityResponseType> {
    return this.http.patch<IEventCategory>(`${this.resourceUrl}/${getEventCategoryIdentifier(eventCategory) as number}`, eventCategory, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEventCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEventCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  enableUpdate(id: number): Observable<HttpResponse<number>> {
    const options = createRequestOption(id);
    return this.http.get<number>(`${this.resourceUrl}/upd/${id}`, { params: options, observe: 'response' });
  }

  addEventCategoryToCollectionIfMissing(
    eventCategoryCollection: IEventCategory[],
    ...eventCategoriesToCheck: (IEventCategory | null | undefined)[]
  ): IEventCategory[] {
    const eventCategories: IEventCategory[] = eventCategoriesToCheck.filter(isPresent);
    if (eventCategories.length > 0) {
      const eventCategoryCollectionIdentifiers = eventCategoryCollection.map(
        eventCategoryItem => getEventCategoryIdentifier(eventCategoryItem)!
      );
      const eventCategoriesToAdd = eventCategories.filter(eventCategoryItem => {
        const eventCategoryIdentifier = getEventCategoryIdentifier(eventCategoryItem);
        if (eventCategoryIdentifier == null || eventCategoryCollectionIdentifiers.includes(eventCategoryIdentifier)) {
          return false;
        }
        eventCategoryCollectionIdentifiers.push(eventCategoryIdentifier);
        return true;
      });
      return [...eventCategoriesToAdd, ...eventCategoryCollection];
    }
    return eventCategoryCollection;
  }
}
