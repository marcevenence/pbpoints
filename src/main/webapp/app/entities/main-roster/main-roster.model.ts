import { ITeam } from 'app/entities/team/team.model';
import { IUserExtra } from 'app/entities/user-extra/user-extra.model';

export interface IMainRoster {
  id?: number;
  team?: ITeam | null;
  userExtra?: IUserExtra | null;
}

export class MainRoster implements IMainRoster {
  constructor(public id?: number, public team?: ITeam | null, public userExtra?: IUserExtra | null) {}
}

export function getMainRosterIdentifier(mainRoster: IMainRoster): number | undefined {
  return mainRoster.id;
}
