import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBracket, getBracketIdentifier } from '../bracket.model';

export type EntityResponseType = HttpResponse<IBracket>;
export type EntityArrayResponseType = HttpResponse<IBracket[]>;

@Injectable({ providedIn: 'root' })
export class BracketService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/brackets');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(bracket: IBracket): Observable<EntityResponseType> {
    return this.http.post<IBracket>(this.resourceUrl, bracket, { observe: 'response' });
  }

  update(bracket: IBracket): Observable<EntityResponseType> {
    return this.http.put<IBracket>(`${this.resourceUrl}/${getBracketIdentifier(bracket) as number}`, bracket, { observe: 'response' });
  }

  partialUpdate(bracket: IBracket): Observable<EntityResponseType> {
    return this.http.patch<IBracket>(`${this.resourceUrl}/${getBracketIdentifier(bracket) as number}`, bracket, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBracket>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBracket[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBracketToCollectionIfMissing(bracketCollection: IBracket[], ...bracketsToCheck: (IBracket | null | undefined)[]): IBracket[] {
    const brackets: IBracket[] = bracketsToCheck.filter(isPresent);
    if (brackets.length > 0) {
      const bracketCollectionIdentifiers = bracketCollection.map(bracketItem => getBracketIdentifier(bracketItem)!);
      const bracketsToAdd = brackets.filter(bracketItem => {
        const bracketIdentifier = getBracketIdentifier(bracketItem);
        if (bracketIdentifier == null || bracketCollectionIdentifiers.includes(bracketIdentifier)) {
          return false;
        }
        bracketCollectionIdentifiers.push(bracketIdentifier);
        return true;
      });
      return [...bracketsToAdd, ...bracketCollection];
    }
    return bracketCollection;
  }
}
