import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IEmpregado, defaultValue } from 'app/shared/model/empregado.model';

export const ACTION_TYPES = {
  FETCH_EMPREGADO_LIST: 'empregado/FETCH_EMPREGADO_LIST',
  FETCH_EMPREGADO: 'empregado/FETCH_EMPREGADO',
  CREATE_EMPREGADO: 'empregado/CREATE_EMPREGADO',
  UPDATE_EMPREGADO: 'empregado/UPDATE_EMPREGADO',
  DELETE_EMPREGADO: 'empregado/DELETE_EMPREGADO',
  RESET: 'empregado/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IEmpregado>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type EmpregadoState = Readonly<typeof initialState>;

// Reducer

export default (state: EmpregadoState = initialState, action): EmpregadoState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_EMPREGADO_LIST):
    case REQUEST(ACTION_TYPES.FETCH_EMPREGADO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_EMPREGADO):
    case REQUEST(ACTION_TYPES.UPDATE_EMPREGADO):
    case REQUEST(ACTION_TYPES.DELETE_EMPREGADO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_EMPREGADO_LIST):
    case FAILURE(ACTION_TYPES.FETCH_EMPREGADO):
    case FAILURE(ACTION_TYPES.CREATE_EMPREGADO):
    case FAILURE(ACTION_TYPES.UPDATE_EMPREGADO):
    case FAILURE(ACTION_TYPES.DELETE_EMPREGADO):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_EMPREGADO_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_EMPREGADO):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_EMPREGADO):
    case SUCCESS(ACTION_TYPES.UPDATE_EMPREGADO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_EMPREGADO):
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

const apiUrl = 'api/empregados';

// Actions

export const getEntities: ICrudGetAllAction<IEmpregado> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_EMPREGADO_LIST,
  payload: axios.get<IEmpregado>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IEmpregado> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_EMPREGADO,
    payload: axios.get<IEmpregado>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IEmpregado> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_EMPREGADO,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IEmpregado> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_EMPREGADO,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IEmpregado> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_EMPREGADO,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
