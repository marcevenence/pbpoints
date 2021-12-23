import { ITeamDetailPoint } from 'app/entities/team-detail-point/team-detail-point.model';
import { IEventCategory } from 'app/entities/event-category/event-category.model';

export interface ITeamCategoryPoint {
  id?: number;
  points?: number | null;
  position?: number | null;
  teamDetailPoint?: ITeamDetailPoint | null;
  eventCategory?: IEventCategory | null;
}

export class TeamCategoryPoint implements ITeamCategoryPoint {
  constructor(
    public id?: number,
    public points?: number | null,
    public position?: number | null,
    public teamDetailPoint?: ITeamDetailPoint | null,
    public eventCategory?: IEventCategory | null
  ) {}
}

export function getTeamCategoryPointIdentifier(teamCategoryPoint: ITeamCategoryPoint): number | undefined {
  return teamCategoryPoint.id;
}
