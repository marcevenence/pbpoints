import { IProvince } from 'app/entities/province/province.model';

export interface ILocation {
  id?: number;
  name?: string | null;
  province?: IProvince | null;
}

export class Location implements ILocation {
  constructor(public id?: number, public name?: string | null, public province?: IProvince | null) {}
}

export function getLocationIdentifier(location: ILocation): number | undefined {
  return location.id;
}
