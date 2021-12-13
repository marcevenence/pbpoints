import * as dayjs from 'dayjs';
import { IEvent } from 'app/entities/event/event.model';
import { IUser } from 'app/entities/user/user.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface ITournament {
  id?: number;
  name?: string | null;
  closeInscrDays?: number | null;
  status?: Status | null;
  categorize?: boolean | null;
  logoContentType?: string | null;
  logo?: string | null;
  cantPlayersNextCategory?: number | null;
  qtyTeamGroups?: number | null;
  startSeason?: dayjs.Dayjs;
  endSeason?: dayjs.Dayjs;
  events?: IEvent[] | null;
  owner?: IUser;
}

export class Tournament implements ITournament {
  constructor(
    public id?: number,
    public name?: string | null,
    public closeInscrDays?: number | null,
    public status?: Status | null,
    public categorize?: boolean | null,
    public logoContentType?: string | null,
    public logo?: string | null,
    public cantPlayersNextCategory?: number | null,
    public qtyTeamGroups?: number | null,
    public startSeason?: dayjs.Dayjs,
    public endSeason?: dayjs.Dayjs,
    public events?: IEvent[] | null,
    public owner?: IUser
  ) {
    this.categorize = this.categorize ?? false;
  }
}

export function getTournamentIdentifier(tournament: ITournament): number | undefined {
  return tournament.id;
}
