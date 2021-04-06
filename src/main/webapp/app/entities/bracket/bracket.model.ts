export interface IBracket {
  id?: number;
  teams?: number;
  teams5A?: number;
  teams5B?: number;
  teams6A?: number;
  teams6B?: number;
}

export class Bracket implements IBracket {
  constructor(
    public id?: number,
    public teams?: number,
    public teams5A?: number,
    public teams5B?: number,
    public teams6A?: number,
    public teams6B?: number
  ) {}
}

export function getBracketIdentifier(bracket: IBracket): number | undefined {
  return bracket.id;
}
