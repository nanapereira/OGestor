import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './lotacao.reducer';
import { ILotacao } from 'app/shared/model/lotacao.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ILotacaoUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const LotacaoUpdate = (props: ILotacaoUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { lotacaoEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/lotacao');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...lotacaoEntity,
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
          <h2 id="oGestorApp.lotacao.home.createOrEditLabel">
            <Translate contentKey="oGestorApp.lotacao.home.createOrEditLabel">Create or edit a Lotacao</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : lotacaoEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="lotacao-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="lotacao-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="codigoLabel" for="lotacao-codigo">
                  <Translate contentKey="oGestorApp.lotacao.codigo">Codigo</Translate>
                </Label>
                <AvField id="lotacao-codigo" type="string" className="form-control" name="codigo" />
              </AvGroup>
              <AvGroup>
                <Label id="nomeLabel" for="lotacao-nome">
                  <Translate contentKey="oGestorApp.lotacao.nome">Nome</Translate>
                </Label>
                <AvField id="lotacao-nome" type="text" name="nome" />
              </AvGroup>
              <AvGroup>
                <Label id="descricaoLabel" for="lotacao-descricao">
                  <Translate contentKey="oGestorApp.lotacao.descricao">Descricao</Translate>
                </Label>
                <AvField id="lotacao-descricao" type="text" name="descricao" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/lotacao" replace color="info">
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
  lotacaoEntity: storeState.lotacao.entity,
  loading: storeState.lotacao.loading,
  updating: storeState.lotacao.updating,
  updateSuccess: storeState.lotacao.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(LotacaoUpdate);
