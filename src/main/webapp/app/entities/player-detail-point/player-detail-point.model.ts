import { IPlayerPoint } from 'app/entities/player-point/player-point.model';
import { IEventCategory } from 'app/entities/event-category/event-category.model';

export interface IPlayerDetailPoint {
  id?: number;
  points?: number;
  playerPoint?: IPlayerPoint;
  eventCategory?: IEventCategory | null;
}

export class PlayerDetailPoint implements IPlayerDetailPoint {
  constructor(
    public id?: number,
    public points?: number,
    public playerPoint?: IPlayerPoint,
    public eventCategory?: IEventCategory | null
  ) {}
}

export function getPlayerDetailPointIdentifier(playerDetailPoint: IPlayerDetailPoint): number | undefined {
  return playerDetailPoint.id;
}
