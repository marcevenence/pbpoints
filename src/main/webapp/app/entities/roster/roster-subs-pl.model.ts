import { IPlayer } from 'app/entities/player/player.model';

export interface IRosterSubsPl {
  player?: IPlayer | null;
  players?: IPlayer[] | null;
}

export class RosterSubsPl implements IRosterSubsPl {
  constructor(public player?: IPlayer, public players?: IPlayer[]) {}
}
