import * as dayjs from 'dayjs';

export class Account {
  constructor(
    public id: number,
    public activated: boolean,
    public authorities: string[],
    public email: string,
    public firstName: string | null,
    public langKey: string,
    public lastName: string | null,
    public login?: string,
    public imageUrl?: string | null,
    public numDoc?: string,
    public phone?: string,
    public bornDate?: dayjs.Dayjs | null,
    public picture?: any,
    public pictureContentType?: string
  ) {}
}
