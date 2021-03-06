export interface IUser {
  id?: number;
  login?: string;
  lastName?: string;
  firstName?: string;
}

export class User implements IUser {
  constructor(public id: number, public login: string) {}
}

export function getUserIdentifier(user: IUser): number | undefined {
  return user.id;
}
