import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';

export interface ISuspension {
  id?: number;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  user?: IUser | null;
}

export class Suspension implements ISuspension {
  constructor(public id?: number, public startDate?: dayjs.Dayjs | null, public endDate?: dayjs.Dayjs | null, public user?: IUser | null) {}
}

export function getSuspensionIdentifier(suspension: ISuspension): number | undefined {
  return suspension.id;
}
