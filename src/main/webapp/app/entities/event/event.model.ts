import * as dayjs from 'dayjs';
import { ICity } from 'app/entities/city/city.model';
import { ITournament } from 'app/entities/tournament/tournament.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IEvent {
  id?: number;
  name?: string | null;
  fromDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  endInscriptionDate?: dayjs.Dayjs | null;
  status?: Status | null;
  createDate?: dayjs.Dayjs | null;
  updatedDate?: dayjs.Dayjs | null;
  city?: ICity | null;
  tournament?: ITournament | null;
}

export class Event implements IEvent {
  constructor(
    public id?: number,
    public name?: string | null,
    public fromDate?: dayjs.Dayjs | null,
    public endDate?: dayjs.Dayjs | null,
    public endInscriptionDate?: dayjs.Dayjs | null,
    public status?: Status | null,
    public createDate?: dayjs.Dayjs | null,
    public updatedDate?: dayjs.Dayjs | null,
    public city?: ICity | null,
    public tournament?: ITournament | null
  ) {}
}

export function getEventIdentifier(event: IEvent): number | undefined {
  return event.id;
}
