import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ILotacao, defaultValue } from 'app/shared/model/lotacao.model';

export const ACTION_TYPES = {
  FETCH_LOTACAO_LIST: 'lotacao/FETCH_LOTACAO_LIST',
  FETCH_LOTACAO: 'lotacao/FETCH_LOTACAO',
  CREATE_LOTACAO: 'lotacao/CREATE_LOTACAO',
  UPDATE_LOTACAO: 'lotacao/UPDATE_LOTACAO',
  DELETE_LOTACAO: 'lotacao/DELETE_LOTACAO',
  RESET: 'lotacao/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ILotacao>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type LotacaoState = Readonly<typeof initialState>;

// Reducer

export default (state: LotacaoState = initialState, action): LotacaoState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_LOTACAO_LIST):
    case REQUEST(ACTION_TYPES.FETCH_LOTACAO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_LOTACAO):
    case REQUEST(ACTION_TYPES.UPDATE_LOTACAO):
    case REQUEST(ACTION_TYPES.DELETE_LOTACAO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_LOTACAO_LIST):
    case FAILURE(ACTION_TYPES.FETCH_LOTACAO):
    case FAILURE(ACTION_TYPES.CREATE_LOTACAO):
    case FAILURE(ACTION_TYPES.UPDATE_LOTACAO):
    case FAILURE(ACTION_TYPES.DELETE_LOTACAO):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_LOTACAO_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_LOTACAO):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_LOTACAO):
    case SUCCESS(ACTION_TYPES.UPDATE_LOTACAO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_LOTACAO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/lotacaos';

// Actions

export const getEntities: ICrudGetAllAction<ILotacao> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_LOTACAO_LIST,
  payload: axios.get<ILotacao>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ILotacao> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_LOTACAO,
    payload: axios.get<ILotacao>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ILotacao> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_LOTACAO,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ILotacao> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_LOTACAO,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ILotacao> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_LOTACAO,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
