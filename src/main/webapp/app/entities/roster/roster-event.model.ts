export interface IRosterEvent {
  id?: string | null;
  tournamentId?: number | null;
  eventId?: number | null;
  eventName?: string | null;
  anio?: number | null;
  evCatId?: number | null;
  categoryName?: string | null;
  teamId?: number | null;
  teamName?: string | null;
  teamLogo?: string | null;
  teamLogoContentType?: string;
  playerId?: number | null;
  pbPointId?: number | null;
  playerName?: string | null;
  playerPicture?: string | null;
  playerPictureContentType?: string | null;
  playerCategory?: string | null;
  playerProfile?: string | null;
  playerDoc?: string | null;
}

export class RosterEvent implements IRosterEvent {
  constructor(
    public id?: string | null,
    public tournamentId?: number | null,
    public eventId?: number | null,
    public eventName?: string | null,
    public anio?: number | null,
    public evCatId?: number | null,
    public categoryName?: string | null,
    public teamId?: number | null,
    public teamName?: string | null,
    public teamLogo?: string | null,
    public teamLogoContentType?: string,
    public playerId?: number | null,
    public pbPointId?: number | null,
    public playerName?: string | null,
    public playerPicture?: string | null,
    public playerPictureContentType?: string | null,
    public playerCategory?: string | null,
    public playerProfile?: string | null,
    public playerDoc?: string | null
  ) {}
}
