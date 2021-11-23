import { ITournament } from 'app/entities/tournament/tournament.model';

export interface ITournamentGroup {
  id?: number;
  name?: string;
  tournamentA?: ITournament;
  tournamentB?: ITournament;
}

export class TournamentGroup implements ITournamentGroup {
  constructor(public id?: number, public name?: string, public tournamentA?: ITournament, public tournamentB?: ITournament) {}
}

export function getTournamentGroupIdentifier(tournamentGroup: ITournamentGroup): number | undefined {
  return tournamentGroup.id;
}
