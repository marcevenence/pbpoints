import { IPlayer } from 'app/entities/player/player.model';
import { IRoster } from 'app/entities/roster/roster.model';

export interface IRosterSubs {
  id?: number;
  code?: string;
  profile?: string;
  eventCategoryId?: number;
  roster?: IRoster;
  players?: IPlayer[] | null;
}

export class RosterSubs implements IRosterSubs {
  constructor(
    public id?: number,
    public code?: string,
    public profile?: string,
    public eventCategoryId?: number,
    public roster?: IRoster,
    public players?: IPlayer[]
  ) {}
}
