import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IDocType } from 'app/entities/doc-type/doc-type.model';

export interface IUserExtra {
  id?: number;
  numDoc?: string | null;
  phone?: string | null;
  bornDate?: dayjs.Dayjs | null;
  pictureContentType?: string;
  picture?: string;
  code?: string;
  user?: IUser;
  docType?: IDocType | null;
}

export class UserExtra implements IUserExtra {
  constructor(
    public id?: number,
    public numDoc?: string | null,
    public phone?: string | null,
    public bornDate?: dayjs.Dayjs | null,
    public pictureContentType?: string,
    public picture?: string,
    public code?: string,
    public user?: IUser,
    public docType?: IDocType | null
  ) {}
}

export function getUserExtraIdentifier(userExtra: IUserExtra): number | undefined {
  return userExtra.id;
}
