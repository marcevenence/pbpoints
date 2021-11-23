import { ICity } from 'app/entities/city/city.model';

export interface IField {
  id?: number;
  name?: string;
  city?: ICity | null;
}

export class Field implements IField {
  constructor(public id?: number, public name?: string, public city?: ICity | null) {}
}

export function getFieldIdentifier(field: IField): number | undefined {
  return field.id;
}
