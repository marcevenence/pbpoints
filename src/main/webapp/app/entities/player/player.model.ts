import { IUser } from 'app/entities/user/user.model';
import { IRoster } from 'app/entities/roster/roster.model';
import { ICategory } from 'app/entities/category/category.model';
import { ProfileUser } from 'app/entities/enumerations/profile-user.model';

export interface IPlayer {
  id?: number;
  profile?: ProfileUser | null;
  user?: IUser | null;
  roster?: IRoster;
  category?: ICategory | null;
}

export class Player implements IPlayer {
  constructor(
    public id?: number,
    public profile?: ProfileUser | null,
    public user?: IUser | null,
    public roster?: IRoster,
    public category?: ICategory | null
  ) {}
}

export function getPlayerIdentifier(player: IPlayer): number | undefined {
  return player.id;
}
