import { IEvent } from 'app/entities/event/event.model';
import { IPlayerPoint } from 'app/entities/player-point/player-point.model';

export interface IPlayerDetailPoint {
  id?: number;
  points?: number;
  event?: IEvent;
  playerPoint?: IPlayerPoint;
}

export class PlayerDetailPoint implements IPlayerDetailPoint {
  constructor(public id?: number, public points?: number, public event?: IEvent, public playerPoint?: IPlayerPoint) {}
}

export function getPlayerDetailPointIdentifier(playerDetailPoint: IPlayerDetailPoint): number | undefined {
  return playerDetailPoint.id;
}
