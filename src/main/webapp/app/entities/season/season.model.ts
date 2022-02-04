import { ITournament } from 'app/entities/tournament/tournament.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface ISeason {
  id?: number;
  anio?: number;
  status?: Status;
  tournament?: ITournament;
}

export class Season implements ISeason {
  constructor(public id?: number, public anio?: number, public status?: Status, public tournament?: ITournament) {}
}

export function getSeasonIdentifier(season: ISeason): number | undefined {
  return season.id;
}
