import { ITeam } from 'app/entities/team/team.model';
import { IEventCategory } from 'app/entities/event-category/event-category.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IGame {
  id?: number;
  pointsA?: number | null;
  pointsB?: number | null;
  splitDeckNum?: number | null;
  timeLeft?: number | null;
  status?: Status;
  overtimeA?: number | null;
  overtimeB?: number | null;
  uvuA?: number | null;
  uvuB?: number | null;
  group?: number | null;
  clasif?: string | null;
  teamA?: ITeam;
  teamB?: ITeam;
  eventCategory?: IEventCategory;
}

export class Game implements IGame {
  constructor(
    public id?: number,
    public pointsA?: number | null,
    public pointsB?: number | null,
    public splitDeckNum?: number | null,
    public timeLeft?: number | null,
    public status?: Status,
    public overtimeA?: number | null,
    public overtimeB?: number | null,
    public uvuA?: number | null,
    public uvuB?: number | null,
    public group?: number | null,
    public clasif?: string | null,
    public teamA?: ITeam,
    public teamB?: ITeam,
    public eventCategory?: IEventCategory
  ) {}
}

export function getGameIdentifier(game: IGame): number | undefined {
  return game.id;
}
