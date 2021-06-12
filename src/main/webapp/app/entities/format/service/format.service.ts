import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFormat, getFormatIdentifier } from '../format.model';

export type EntityResponseType = HttpResponse<IFormat>;
export type EntityArrayResponseType = HttpResponse<IFormat[]>;

@Injectable({ providedIn: 'root' })
export class FormatService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/formats');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(format: IFormat): Observable<EntityResponseType> {
    return this.http.post<IFormat>(this.resourceUrl, format, { observe: 'response' });
  }

  update(format: IFormat): Observable<EntityResponseType> {
    return this.http.put<IFormat>(`${this.resourceUrl}`, format, { observe: 'response' });
  }

  partialUpdate(format: IFormat): Observable<EntityResponseType> {
    return this.http.patch<IFormat>(`${this.resourceUrl}/${getFormatIdentifier(format) as number}`, format, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFormat>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFormat[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFormatToCollectionIfMissing(formatCollection: IFormat[], ...formatsToCheck: (IFormat | null | undefined)[]): IFormat[] {
    const formats: IFormat[] = formatsToCheck.filter(isPresent);
    if (formats.length > 0) {
      const formatCollectionIdentifiers = formatCollection.map(formatItem => getFormatIdentifier(formatItem)!);
      const formatsToAdd = formats.filter(formatItem => {
        const formatIdentifier = getFormatIdentifier(formatItem);
        if (formatIdentifier == null || formatCollectionIdentifiers.includes(formatIdentifier)) {
          return false;
        }
        formatCollectionIdentifiers.push(formatIdentifier);
        return true;
      });
      return [...formatsToAdd, ...formatCollection];
    }
    return formatCollection;
  }
}
