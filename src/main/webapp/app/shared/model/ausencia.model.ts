import { IEmpregado } from 'app/shared/model/empregado.model';
import { TipoAusencia } from 'app/shared/model/enumerations/tipo-ausencia.model';

export interface IAusencia {
  id?: number;
  tipo?: TipoAusencia;
  descricao?: string;
  dataInicio?: string;
  dataFim?: string;
  empregado?: IEmpregado;
}

export const defaultValue: Readonly<IAusencia> = {};
