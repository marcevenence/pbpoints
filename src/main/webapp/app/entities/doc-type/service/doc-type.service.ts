import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDocType, getDocTypeIdentifier } from '../doc-type.model';

export type EntityResponseType = HttpResponse<IDocType>;
export type EntityArrayResponseType = HttpResponse<IDocType[]>;

@Injectable({ providedIn: 'root' })
export class DocTypeService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/doc-types');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(docType: IDocType): Observable<EntityResponseType> {
    return this.http.post<IDocType>(this.resourceUrl, docType, { observe: 'response' });
  }

  update(docType: IDocType): Observable<EntityResponseType> {
    return this.http.put<IDocType>(`${this.resourceUrl}/${getDocTypeIdentifier(docType) as number}`, docType, { observe: 'response' });
  }

  partialUpdate(docType: IDocType): Observable<EntityResponseType> {
    return this.http.patch<IDocType>(`${this.resourceUrl}/${getDocTypeIdentifier(docType) as number}`, docType, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDocType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDocType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDocTypeToCollectionIfMissing(docTypeCollection: IDocType[], ...docTypesToCheck: (IDocType | null | undefined)[]): IDocType[] {
    const docTypes: IDocType[] = docTypesToCheck.filter(isPresent);
    if (docTypes.length > 0) {
      const docTypeCollectionIdentifiers = docTypeCollection.map(docTypeItem => getDocTypeIdentifier(docTypeItem)!);
      const docTypesToAdd = docTypes.filter(docTypeItem => {
        const docTypeIdentifier = getDocTypeIdentifier(docTypeItem);
        if (docTypeIdentifier == null || docTypeCollectionIdentifiers.includes(docTypeIdentifier)) {
          return false;
        }
        docTypeCollectionIdentifiers.push(docTypeIdentifier);
        return true;
      });
      return [...docTypesToAdd, ...docTypeCollection];
    }
    return docTypeCollection;
  }
}
