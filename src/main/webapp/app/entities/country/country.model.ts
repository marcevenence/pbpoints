import { IProvince } from 'app/entities/province/province.model';

export interface ICountry {
  id?: number;
  name?: string | null;
  provinces?: IProvince[] | null;
}

export class Country implements ICountry {
  constructor(public id?: number, public name?: string | null, public provinces?: IProvince[] | null) {}
}

export function getCountryIdentifier(country: ICountry): number | undefined {
  return country.id;
}
