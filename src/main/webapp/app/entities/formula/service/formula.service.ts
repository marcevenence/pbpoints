import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFormula, getFormulaIdentifier } from '../formula.model';

export type EntityResponseType = HttpResponse<IFormula>;
export type EntityArrayResponseType = HttpResponse<IFormula[]>;

@Injectable({ providedIn: 'root' })
export class FormulaService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/formulas');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(formula: IFormula): Observable<EntityResponseType> {
    return this.http.post<IFormula>(this.resourceUrl, formula, { observe: 'response' });
  }

  update(formula: IFormula): Observable<EntityResponseType> {
    return this.http.put<IFormula>(`${this.resourceUrl}/${getFormulaIdentifier(formula) as number}`, formula, { observe: 'response' });
  }

  partialUpdate(formula: IFormula): Observable<EntityResponseType> {
    return this.http.patch<IFormula>(`${this.resourceUrl}/${getFormulaIdentifier(formula) as number}`, formula, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFormula>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFormula[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFormulaToCollectionIfMissing(formulaCollection: IFormula[], ...formulasToCheck: (IFormula | null | undefined)[]): IFormula[] {
    const formulas: IFormula[] = formulasToCheck.filter(isPresent);
    if (formulas.length > 0) {
      const formulaCollectionIdentifiers = formulaCollection.map(formulaItem => getFormulaIdentifier(formulaItem)!);
      const formulasToAdd = formulas.filter(formulaItem => {
        const formulaIdentifier = getFormulaIdentifier(formulaItem);
        if (formulaIdentifier == null || formulaCollectionIdentifiers.includes(formulaIdentifier)) {
          return false;
        }
        formulaCollectionIdentifiers.push(formulaIdentifier);
        return true;
      });
      return [...formulasToAdd, ...formulaCollection];
    }
    return formulaCollection;
  }
}
