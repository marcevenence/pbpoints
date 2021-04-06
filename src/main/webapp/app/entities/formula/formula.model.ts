import { ITournament } from 'app/entities/tournament/tournament.model';

export interface IFormula {
  id?: number;
  formula?: string;
  var1?: string;
  var2?: string | null;
  var3?: string | null;
  description?: string;
  example?: string | null;
  tournament?: ITournament | null;
}

export class Formula implements IFormula {
  constructor(
    public id?: number,
    public formula?: string,
    public var1?: string,
    public var2?: string | null,
    public var3?: string | null,
    public description?: string,
    public example?: string | null,
    public tournament?: ITournament | null
  ) {}
}

export function getFormulaIdentifier(formula: IFormula): number | undefined {
  return formula.id;
}
