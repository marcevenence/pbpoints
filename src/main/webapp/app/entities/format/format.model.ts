import { ITournament } from 'app/entities/tournament/tournament.model';

export interface IFormat {
  id?: number;
  name?: string;
  description?: string | null;
  playersQty?: number | null;
  tournament?: ITournament;
}

export class Format implements IFormat {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string | null,
    public playersQty?: number | null,
    public tournament?: ITournament
  ) {}
}

export function getFormatIdentifier(format: IFormat): number | undefined {
  return format.id;
}
