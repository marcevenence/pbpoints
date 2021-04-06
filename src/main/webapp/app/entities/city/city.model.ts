import { IProvince } from 'app/entities/province/province.model';

export interface ICity {
  id?: number;
  name?: string | null;
  latitude?: string | null;
  longitude?: string | null;
  province?: IProvince | null;
}

export class City implements ICity {
  constructor(
    public id?: number,
    public name?: string | null,
    public latitude?: string | null,
    public longitude?: string | null,
    public province?: IProvince | null
  ) {}
}

export function getCityIdentifier(city: ICity): number | undefined {
  return city.id;
}
