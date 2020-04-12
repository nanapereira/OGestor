import { IEmpregado } from 'app/shared/model/empregado.model';

export interface IProjeto {
  id?: number;
  nome?: string;
  descricao?: string;
  dataInicio?: string;
  dataFim?: string;
  gestor?: IEmpregado;
  empregados?: IEmpregado[];
  listaempregados?: IEmpregado[];
}

export const defaultValue: Readonly<IProjeto> = {};
