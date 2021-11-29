import { IUser } from 'app/entities/user/user.model';

export interface IEquipment {
  id?: number;
  brand?: string;
  model?: string;
  picture1ContentType?: string;
  picture1?: string;
  picture2ContentType?: string;
  picture2?: string;
  serial?: string | null;
  user?: IUser | null;
}

export class Equipment implements IEquipment {
  constructor(
    public id?: number,
    public brand?: string,
    public model?: string,
    public picture1ContentType?: string,
    public picture1?: string,
    public picture2ContentType?: string,
    public picture2?: string,
    public serial?: string | null,
    public user?: IUser | null
  ) {}
}

export function getEquipmentIdentifier(equipment: IEquipment): number | undefined {
  return equipment.id;
}
