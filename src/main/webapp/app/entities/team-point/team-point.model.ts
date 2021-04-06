import { ITeam } from 'app/entities/team/team.model';
import { ITournament } from 'app/entities/tournament/tournament.model';

export interface ITeamPoint {
  id?: number;
  points?: number;
  team?: ITeam;
  tournament?: ITournament;
}

export class TeamPoint implements ITeamPoint {
  constructor(public id?: number, public points?: number, public team?: ITeam, public tournament?: ITournament) {}
}

export function getTeamPointIdentifier(teamPoint: ITeamPoint): number | undefined {
  return teamPoint.id;
}
