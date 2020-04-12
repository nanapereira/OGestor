import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IEmpregado } from 'app/shared/model/empregado.model';
import { getEntities as getEmpregados } from 'app/entities/empregado/empregado.reducer';
import { getEntity, updateEntity, createEntity, reset } from './ausencia.reducer';
import { IAusencia } from 'app/shared/model/ausencia.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAusenciaUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AusenciaUpdate = (props: IAusenciaUpdateProps) => {
  const [empregadoId, setEmpregadoId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { ausenciaEntity, empregados, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/ausencia');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getEmpregados();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...ausenciaEntity,
        ...values
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="oGestorApp.ausencia.home.createOrEditLabel">
            <Translate contentKey="oGestorApp.ausencia.home.createOrEditLabel">Create or edit a Ausencia</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : ausenciaEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="ausencia-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="ausencia-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="tipoLabel" for="ausencia-tipo">
                  <Translate contentKey="oGestorApp.ausencia.tipo">Tipo</Translate>
                </Label>
                <AvInput
                  id="ausencia-tipo"
                  type="select"
                  className="form-control"
                  name="tipo"
                  value={(!isNew && ausenciaEntity.tipo) || 'ABONO'}
                >
                  <option value="ABONO">{translate('oGestorApp.TipoAusencia.ABONO')}</option>
                  <option value="FERIAS">{translate('oGestorApp.TipoAusencia.FERIAS')}</option>
                  <option value="LICENCA">{translate('oGestorApp.TipoAusencia.LICENCA')}</option>
                  <option value="OUTROS">{translate('oGestorApp.TipoAusencia.OUTROS')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="descricaoLabel" for="ausencia-descricao">
                  <Translate contentKey="oGestorApp.ausencia.descricao">Descricao</Translate>
                </Label>
                <AvField id="ausencia-descricao" type="text" name="descricao" />
              </AvGroup>
              <AvGroup>
                <Label id="dataInicioLabel" for="ausencia-dataInicio">
                  <Translate contentKey="oGestorApp.ausencia.dataInicio">Data Inicio</Translate>
                </Label>
                <AvField id="ausencia-dataInicio" type="text" name="dataInicio" />
              </AvGroup>
              <AvGroup>
                <Label id="dataFimLabel" for="ausencia-dataFim">
                  <Translate contentKey="oGestorApp.ausencia.dataFim">Data Fim</Translate>
                </Label>
                <AvField id="ausencia-dataFim" type="text" name="dataFim" />
              </AvGroup>
              <AvGroup>
                <Label for="ausencia-empregado">
                  <Translate contentKey="oGestorApp.ausencia.empregado">Empregado</Translate>
                </Label>
                <AvInput id="ausencia-empregado" type="select" className="form-control" name="empregado.id">
                  <option value="" key="0" />
                  {empregados
                    ? empregados.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/ausencia" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  empregados: storeState.empregado.entities,
  ausenciaEntity: storeState.ausencia.entity,
  loading: storeState.ausencia.loading,
  updating: storeState.ausencia.updating,
  updateSuccess: storeState.ausencia.updateSuccess
});

const mapDispatchToProps = {
  getEmpregados,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AusenciaUpdate);
