import { ITeamPoint } from 'app/entities/team-point/team-point.model';
import { IEvent } from 'app/entities/event/event.model';

export interface ITeamDetailPoint {
  id?: number;
  points?: number;
  teamPoint?: ITeamPoint;
  event?: IEvent;
}

export class TeamDetailPoint implements ITeamDetailPoint {
  constructor(public id?: number, public points?: number, public teamPoint?: ITeamPoint, public event?: IEvent) {}
}

export function getTeamDetailPointIdentifier(teamDetailPoint: ITeamDetailPoint): number | undefined {
  return teamDetailPoint.id;
}
