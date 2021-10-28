import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISponsor, getSponsorIdentifier } from '../sponsor.model';

export type EntityResponseType = HttpResponse<ISponsor>;
export type EntityArrayResponseType = HttpResponse<ISponsor[]>;

@Injectable({ providedIn: 'root' })
export class SponsorService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/sponsors');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(sponsor: ISponsor): Observable<EntityResponseType> {
    return this.http.post<ISponsor>(this.resourceUrl, sponsor, { observe: 'response' });
  }

  update(sponsor: ISponsor): Observable<EntityResponseType> {
    return this.http.put<ISponsor>(`${this.resourceUrl}/${getSponsorIdentifier(sponsor) as number}`, sponsor, { observe: 'response' });
  }

  partialUpdate(sponsor: ISponsor): Observable<EntityResponseType> {
    return this.http.patch<ISponsor>(`${this.resourceUrl}/${getSponsorIdentifier(sponsor) as number}`, sponsor, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISponsor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISponsor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSponsorToCollectionIfMissing(sponsorCollection: ISponsor[], ...sponsorsToCheck: (ISponsor | null | undefined)[]): ISponsor[] {
    const sponsors: ISponsor[] = sponsorsToCheck.filter(isPresent);
    if (sponsors.length > 0) {
      const sponsorCollectionIdentifiers = sponsorCollection.map(sponsorItem => getSponsorIdentifier(sponsorItem)!);
      const sponsorsToAdd = sponsors.filter(sponsorItem => {
        const sponsorIdentifier = getSponsorIdentifier(sponsorItem);
        if (sponsorIdentifier == null || sponsorCollectionIdentifiers.includes(sponsorIdentifier)) {
          return false;
        }
        sponsorCollectionIdentifiers.push(sponsorIdentifier);
        return true;
      });
      return [...sponsorsToAdd, ...sponsorCollection];
    }
    return sponsorCollection;
  }
}
