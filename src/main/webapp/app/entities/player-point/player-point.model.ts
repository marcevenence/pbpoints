import { ITournament } from 'app/entities/tournament/tournament.model';
import { IUser } from 'app/entities/user/user.model';
import { ICategory } from 'app/entities/category/category.model';

export interface IPlayerPoint {
  id?: number;
  points?: number;
  tournament?: ITournament;
  user?: IUser;
  category?: ICategory | null;
}

export class PlayerPoint implements IPlayerPoint {
  constructor(
    public id?: number,
    public points?: number,
    public tournament?: ITournament,
    public user?: IUser,
    public category?: ICategory | null
  ) {}
}

export function getPlayerPointIdentifier(playerPoint: IPlayerPoint): number | undefined {
  return playerPoint.id;
}
