import {ICompany} from "./ICompany";

export interface ICompanyInvitation {
  id: number,
  company: ICompany,
  participants: number,
  paymentMethod: string
  additionalInfo: string
}
