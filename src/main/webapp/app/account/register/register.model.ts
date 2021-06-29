import { IDocType } from 'app/entities/doc-type/doc-type.model';

export class Registration {
  constructor(
    public login: string,
    public email: string,
    public firstName: string,
    public lastName: string,
    public password: string,
    public langKey: string,
    public phone: string,
    public numDoc: string,
    public bornDate: any,
    public picture: string,
    public pictureContentType: string,
    public code: string,
    public docType: IDocType
  ) {}
}
