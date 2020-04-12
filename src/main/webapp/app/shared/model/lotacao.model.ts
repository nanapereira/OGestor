export interface ILotacao {
  id?: number;
  codigo?: number;
  nome?: string;
  descricao?: string;
}

export const defaultValue: Readonly<ILotacao> = {};
