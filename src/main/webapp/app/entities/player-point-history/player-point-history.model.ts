import { IPlayerPoint } from 'app/entities/player-point/player-point.model';
import { ICategory } from 'app/entities/category/category.model';
import { ISeason } from 'app/entities/season/season.model';

export interface IPlayerPointHistory {
  id?: number;
  points?: number;
  playerPoint?: IPlayerPoint | null;
  category?: ICategory;
  season?: ISeason;
}

export class PlayerPointHistory implements IPlayerPointHistory {
  constructor(
    public id?: number,
    public points?: number,
    public playerPoint?: IPlayerPoint | null,
    public category?: ICategory,
    public season?: ISeason
  ) {}
}

export function getPlayerPointHistoryIdentifier(playerPointHistory: IPlayerPointHistory): number | undefined {
  return playerPointHistory.id;
}
