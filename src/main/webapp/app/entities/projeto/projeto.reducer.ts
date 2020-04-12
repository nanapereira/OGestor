import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IProjeto, defaultValue } from 'app/shared/model/projeto.model';

export const ACTION_TYPES = {
  FETCH_PROJETO_LIST: 'projeto/FETCH_PROJETO_LIST',
  FETCH_PROJETO: 'projeto/FETCH_PROJETO',
  CREATE_PROJETO: 'projeto/CREATE_PROJETO',
  UPDATE_PROJETO: 'projeto/UPDATE_PROJETO',
  DELETE_PROJETO: 'projeto/DELETE_PROJETO',
  RESET: 'projeto/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IProjeto>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ProjetoState = Readonly<typeof initialState>;

// Reducer

export default (state: ProjetoState = initialState, action): ProjetoState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PROJETO_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PROJETO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PROJETO):
    case REQUEST(ACTION_TYPES.UPDATE_PROJETO):
    case REQUEST(ACTION_TYPES.DELETE_PROJETO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_PROJETO_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PROJETO):
    case FAILURE(ACTION_TYPES.CREATE_PROJETO):
    case FAILURE(ACTION_TYPES.UPDATE_PROJETO):
    case FAILURE(ACTION_TYPES.DELETE_PROJETO):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_PROJETO_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PROJETO):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PROJETO):
    case SUCCESS(ACTION_TYPES.UPDATE_PROJETO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PROJETO):
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

const apiUrl = 'api/projetos';

// Actions

export const getEntities: ICrudGetAllAction<IProjeto> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_PROJETO_LIST,
  payload: axios.get<IProjeto>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IProjeto> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PROJETO,
    payload: axios.get<IProjeto>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IProjeto> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PROJETO,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IProjeto> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PROJETO,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IProjeto> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PROJETO,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
