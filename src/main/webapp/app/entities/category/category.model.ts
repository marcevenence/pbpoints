import { ITournament } from 'app/entities/tournament/tournament.model';
import { TimeType } from 'app/entities/enumerations/time-type.model';

export interface ICategory {
  id?: number;
  name?: string | null;
  description?: string | null;
  gameTimeType?: TimeType;
  gameTime?: number;
  stopTimeType?: TimeType;
  stopTime?: number;
  totalPoints?: number;
  difPoints?: number;
  order?: number;
  tournament?: ITournament;
}

export class Category implements ICategory {
  constructor(
    public id?: number,
    public name?: string | null,
    public description?: string | null,
    public gameTimeType?: TimeType,
    public gameTime?: number,
    public stopTimeType?: TimeType,
    public stopTime?: number,
    public totalPoints?: number,
    public difPoints?: number,
    public order?: number,
    public tournament?: ITournament
  ) {}
}

export function getCategoryIdentifier(category: ICategory): number | undefined {
  return category.id;
}
