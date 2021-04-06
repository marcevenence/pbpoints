export interface IDocType {
  id?: number;
  name?: string | null;
  description?: string | null;
}

export class DocType implements IDocType {
  constructor(public id?: number, public name?: string | null, public description?: string | null) {}
}

export function getDocTypeIdentifier(docType: IDocType): number | undefined {
  return docType.id;
}
