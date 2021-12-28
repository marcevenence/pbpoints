import { ITeamPoint } from 'app/entities/team-point/team-point.model';
import { IEventCategory } from 'app/entities/event-category/event-category.model';

export interface ITeamDetailPoint {
  id?: number;
  points?: number;
  teamPoint?: ITeamPoint;
  eventCategory?: IEventCategory;
}

export class TeamDetailPoint implements ITeamDetailPoint {
  constructor(public id?: number, public points?: number, public teamPoint?: ITeamPoint, public eventCategory?: IEventCategory) {}
}

export function getTeamDetailPointIdentifier(teamDetailPoint: ITeamDetailPoint): number | undefined {
  return teamDetailPoint.id;
}
