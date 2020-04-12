import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IAusencia, defaultValue } from 'app/shared/model/ausencia.model';

export const ACTION_TYPES = {
  FETCH_AUSENCIA_LIST: 'ausencia/FETCH_AUSENCIA_LIST',
  FETCH_AUSENCIA: 'ausencia/FETCH_AUSENCIA',
  CREATE_AUSENCIA: 'ausencia/CREATE_AUSENCIA',
  UPDATE_AUSENCIA: 'ausencia/UPDATE_AUSENCIA',
  DELETE_AUSENCIA: 'ausencia/DELETE_AUSENCIA',
  RESET: 'ausencia/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAusencia>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type AusenciaState = Readonly<typeof initialState>;

// Reducer

export default (state: AusenciaState = initialState, action): AusenciaState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_AUSENCIA_LIST):
    case REQUEST(ACTION_TYPES.FETCH_AUSENCIA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_AUSENCIA):
    case REQUEST(ACTION_TYPES.UPDATE_AUSENCIA):
    case REQUEST(ACTION_TYPES.DELETE_AUSENCIA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_AUSENCIA_LIST):
    case FAILURE(ACTION_TYPES.FETCH_AUSENCIA):
    case FAILURE(ACTION_TYPES.CREATE_AUSENCIA):
    case FAILURE(ACTION_TYPES.UPDATE_AUSENCIA):
    case FAILURE(ACTION_TYPES.DELETE_AUSENCIA):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_AUSENCIA_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_AUSENCIA):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_AUSENCIA):
    case SUCCESS(ACTION_TYPES.UPDATE_AUSENCIA):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_AUSENCIA):
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

const apiUrl = 'api/ausencias';

// Actions

export const getEntities: ICrudGetAllAction<IAusencia> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_AUSENCIA_LIST,
  payload: axios.get<IAusencia>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IAusencia> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_AUSENCIA,
    payload: axios.get<IAusencia>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IAusencia> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_AUSENCIA,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAusencia> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_AUSENCIA,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAusencia> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_AUSENCIA,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
