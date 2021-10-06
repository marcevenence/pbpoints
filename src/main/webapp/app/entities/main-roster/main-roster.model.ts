import { ITeam } from 'app/entities/team/team.model';
import { IUser } from 'app/entities/user/user.model';

export interface IMainRoster {
  id?: number;
  team?: ITeam | null;
  user?: IUser | null;
}

export class MainRoster implements IMainRoster {
  constructor(public id?: number, public team?: ITeam | null, public user?: IUser | null) {}
}

export function getMainRosterIdentifier(mainRoster: IMainRoster): number | undefined {
  return mainRoster.id;
}
