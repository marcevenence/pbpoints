import { ICity } from 'app/entities/city/city.model';
import { ICountry } from 'app/entities/country/country.model';

export interface IProvince {
  id?: number;
  name?: string | null;
  cities?: ICity[] | null;
  country?: ICountry | null;
}

export class Province implements IProvince {
  constructor(public id?: number, public name?: string | null, public cities?: ICity[] | null, public country?: ICountry | null) {}
}

export function getProvinceIdentifier(province: IProvince): number | undefined {
  return province.id;
}
