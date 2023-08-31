import {IUser} from "./IUser";

export interface IUserInvitation {
  id: number
  user: IUser
  paymentMethod: string
  additionalInfo: string
}
