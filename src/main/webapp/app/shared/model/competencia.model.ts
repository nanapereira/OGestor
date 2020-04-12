import { IEmpregado } from 'app/shared/model/empregado.model';

export interface ICompetencia {
  id?: number;
  codigo?: number;
  nome?: string;
  descricao?: string;
  empregados?: IEmpregado[];
  listaEmpregados?: IEmpregado[];
}

export const defaultValue: Readonly<ICompetencia> = {};
