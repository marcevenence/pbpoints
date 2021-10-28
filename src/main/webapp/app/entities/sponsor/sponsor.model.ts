import { ITournament } from 'app/entities/tournament/tournament.model';

export interface ISponsor {
  id?: number;
  logoContentType?: string;
  logo?: string;
  name?: string;
  active?: boolean | null;
  tournament?: ITournament | null;
}

export class Sponsor implements ISponsor {
  constructor(
    public id?: number,
    public logoContentType?: string,
    public logo?: string,
    public name?: string,
    public active?: boolean | null,
    public tournament?: ITournament | null
  ) {
    this.active = this.active ?? false;
  }
}

export function getSponsorIdentifier(sponsor: ISponsor): number | undefined {
  return sponsor.id;
}
