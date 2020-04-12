import { ILotacao } from 'app/shared/model/lotacao.model';
import { ICompetencia } from 'app/shared/model/competencia.model';
import { IProjeto } from 'app/shared/model/projeto.model';

export interface IEmpregado {
  id?: number;
  matricula?: string;
  nome?: string;
  cpf?: string;
  email?: string;
  telefone?: string;
  ramal?: string;
  lotacao?: ILotacao;
  competencias?: ICompetencia[];
  projetos?: IProjeto[];
  listaCompetencias?: ICompetencia[];
  listaProjetos?: IProjeto[];
}

export const defaultValue: Readonly<IEmpregado> = {};
