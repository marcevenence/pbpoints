import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEvent, getEventIdentifier } from '../event.model';

export type EntityResponseType = HttpResponse<IEvent>;
export type EntityArrayResponseType = HttpResponse<IEvent[]>;

@Injectable({ providedIn: 'root' })
export class EventService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/events');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(event: IEvent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(event);
    return this.http
      .post<IEvent>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(event: IEvent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(event);
    return this.http
      .put<IEvent>(`${this.resourceUrl}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(event: IEvent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(event);
    return this.http
      .patch<IEvent>(`${this.resourceUrl}/${getEventIdentifier(event) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  generateXML(id: number): any {
    return this.http.get(`${this.resourceUrl}/generateXML/${id}`, { responseType: 'blob' });
  }

  generatePDF(id: number): any {
    return this.http.get(`${this.resourceUrl}/generatePDF/${id}`, { responseType: 'blob' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEvent>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEvent[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEventToCollectionIfMissing(eventCollection: IEvent[], ...eventsToCheck: (IEvent | null | undefined)[]): IEvent[] {
    const events: IEvent[] = eventsToCheck.filter(isPresent);
    if (events.length > 0) {
      const eventCollectionIdentifiers = eventCollection.map(eventItem => getEventIdentifier(eventItem)!);
      const eventsToAdd = events.filter(eventItem => {
        const eventIdentifier = getEventIdentifier(eventItem);
        if (eventIdentifier == null || eventCollectionIdentifiers.includes(eventIdentifier)) {
          return false;
        }
        eventCollectionIdentifiers.push(eventIdentifier);
        return true;
      });
      return [...eventsToAdd, ...eventCollection];
    }
    return eventCollection;
  }

  queryOne(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEvent>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  submit(file: File): Observable<HttpResponse<any>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<IEvent>(`${this.resourceUrl}/importXML`, formData, { observe: 'response' });
  }

  protected convertDateFromClient(event: IEvent): IEvent {
    return Object.assign({}, event, {
      fromDate: event.fromDate?.isValid() ? event.fromDate.format(DATE_FORMAT) : undefined,
      endDate: event.endDate?.isValid() ? event.endDate.format(DATE_FORMAT) : undefined,
      endInscriptionDate: event.endInscriptionDate?.isValid() ? event.endInscriptionDate.format(DATE_FORMAT) : undefined,
      createDate: event.createDate?.isValid() ? event.createDate.toJSON() : undefined,
      updatedDate: event.updatedDate?.isValid() ? event.updatedDate.toJSON() : undefined,
      endInscriptionPlayersDate: event.endInscriptionPlayersDate?.isValid()
        ? event.endInscriptionPlayersDate.format(DATE_FORMAT)
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fromDate = res.body.fromDate ? dayjs(res.body.fromDate) : undefined;
      res.body.endDate = res.body.endDate ? dayjs(res.body.endDate) : undefined;
      res.body.endInscriptionDate = res.body.endInscriptionDate ? dayjs(res.body.endInscriptionDate) : undefined;
      res.body.createDate = res.body.createDate ? dayjs(res.body.createDate) : undefined;
      res.body.updatedDate = res.body.updatedDate ? dayjs(res.body.updatedDate) : undefined;
      res.body.endInscriptionPlayersDate = res.body.endInscriptionPlayersDate ? dayjs(res.body.endInscriptionPlayersDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((event: IEvent) => {
        event.fromDate = event.fromDate ? dayjs(event.fromDate) : undefined;
        event.endDate = event.endDate ? dayjs(event.endDate) : undefined;
        event.endInscriptionDate = event.endInscriptionDate ? dayjs(event.endInscriptionDate) : undefined;
        event.createDate = event.createDate ? dayjs(event.createDate) : undefined;
        event.updatedDate = event.updatedDate ? dayjs(event.updatedDate) : undefined;
        event.endInscriptionPlayersDate = event.endInscriptionPlayersDate ? dayjs(event.endInscriptionPlayersDate) : undefined;
      });
    }
    return res;
  }
}
