import { IPlayer } from 'app/entities/player/player.model';
import { ITeam } from 'app/entities/team/team.model';
import { IEventCategory } from 'app/entities/event-category/event-category.model';

export interface IRoster {
  id?: number;
  active?: boolean | null;
  players?: IPlayer[] | null;
  team?: ITeam;
  eventCategory?: IEventCategory;
}

export class Roster implements IRoster {
  constructor(
    public id?: number,
    public active?: boolean | null,
    public players?: IPlayer[] | null,
    public team?: ITeam,
    public eventCategory?: IEventCategory
  ) {
    this.active = this.active ?? false;
  }
}

export function getRosterIdentifier(roster: IRoster): number | undefined {
  return roster.id;
}
