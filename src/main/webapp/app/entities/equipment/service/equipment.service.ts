import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEquipment, getEquipmentIdentifier } from '../equipment.model';

export type EntityResponseType = HttpResponse<IEquipment>;
export type EntityArrayResponseType = HttpResponse<IEquipment[]>;

@Injectable({ providedIn: 'root' })
export class EquipmentService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/equipment');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(equipment: IEquipment): Observable<EntityResponseType> {
    return this.http.post<IEquipment>(this.resourceUrl, equipment, { observe: 'response' });
  }

  update(equipment: IEquipment): Observable<EntityResponseType> {
    return this.http.put<IEquipment>(`${this.resourceUrl}/${getEquipmentIdentifier(equipment) as number}`, equipment, {
      observe: 'response',
    });
  }

  partialUpdate(equipment: IEquipment): Observable<EntityResponseType> {
    return this.http.patch<IEquipment>(`${this.resourceUrl}/${getEquipmentIdentifier(equipment) as number}`, equipment, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEquipment>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEquipment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEquipmentToCollectionIfMissing(
    equipmentCollection: IEquipment[],
    ...equipmentToCheck: (IEquipment | null | undefined)[]
  ): IEquipment[] {
    const equipment: IEquipment[] = equipmentToCheck.filter(isPresent);
    if (equipment.length > 0) {
      const equipmentCollectionIdentifiers = equipmentCollection.map(equipmentItem => getEquipmentIdentifier(equipmentItem)!);
      const equipmentToAdd = equipment.filter(equipmentItem => {
        const equipmentIdentifier = getEquipmentIdentifier(equipmentItem);
        if (equipmentIdentifier == null || equipmentCollectionIdentifiers.includes(equipmentIdentifier)) {
          return false;
        }
        equipmentCollectionIdentifiers.push(equipmentIdentifier);
        return true;
      });
      return [...equipmentToAdd, ...equipmentCollection];
    }
    return equipmentCollection;
  }
}
