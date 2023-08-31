import {IUser} from "./IUser";
import {ICompanyInvitation} from "./ICompanyInvitation";
import {IUserInvitation} from "./IUserInvitation";

export interface IEvent {
  id: number
  name: string
  date: Date
  place: string
  additionalInfo: string;
  organizer: IUser
  admins: IUser[]
  companyInvitations: ICompanyInvitation[]
  userInvitations: IUserInvitation[]
}
