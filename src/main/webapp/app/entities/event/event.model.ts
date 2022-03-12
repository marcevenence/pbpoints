import * as dayjs from 'dayjs';
import { IField } from 'app/entities/field/field.model';
import { ITournament } from 'app/entities/tournament/tournament.model';
import { ISeason } from 'app/entities/season/season.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IEvent {
  id?: number;
  name?: string | null;
  fromDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  startInscriptionDate?: dayjs.Dayjs | null;
  endInscriptionDate?: dayjs.Dayjs | null;
  status?: Status | null;
  createDate?: dayjs.Dayjs | null;
  updatedDate?: dayjs.Dayjs | null;
  field?: IField | null;
  endInscriptionPlayersDate?: dayjs.Dayjs;
  tournament?: ITournament | null;
  season?: ISeason | null;
}

export class Event implements IEvent {
  constructor(
    public id?: number,
    public name?: string | null,
    public fromDate?: dayjs.Dayjs | null,
    public endDate?: dayjs.Dayjs | null,
    public startInscriptionDate?: dayjs.Dayjs | null,
    public endInscriptionDate?: dayjs.Dayjs | null,
    public status?: Status | null,
    public createDate?: dayjs.Dayjs | null,
    public updatedDate?: dayjs.Dayjs | null,
    public endInscriptionPlayersDate?: dayjs.Dayjs,
    public tournament?: ITournament | null,
    public field?: IField | null,
    public season?: ISeason | null
  ) {}
}

export function getEventIdentifier(event: IEvent): number | undefined {
  return event.id;
}
