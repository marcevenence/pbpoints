import { IUser } from 'app/entities/user/user.model';

export interface ITeam {
  id?: number;
  name?: string | null;
  active?: boolean | null;
  owner?: IUser | null;
}

export class Team implements ITeam {
  constructor(public id?: number, public name?: string | null, public active?: boolean | null, public owner?: IUser | null) {
    this.active = this.active ?? false;
  }
}

export function getTeamIdentifier(team: ITeam): number | undefined {
  return team.id;
}
