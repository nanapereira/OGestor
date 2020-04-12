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
import { getEntity, updateEntity, createEntity, reset } from './competencia.reducer';
import { ICompetencia } from 'app/shared/model/competencia.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICompetenciaUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CompetenciaUpdate = (props: ICompetenciaUpdateProps) => {
  const [idsempregados, setIdsempregados] = useState([]);
  const [listaEmpregadosId, setListaEmpregadosId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { competenciaEntity, empregados, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/competencia');
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
        ...competenciaEntity,
        ...values,
        empregados: mapIdList(values.empregados)
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
          <h2 id="oGestorApp.competencia.home.createOrEditLabel">
            <Translate contentKey="oGestorApp.competencia.home.createOrEditLabel">Create or edit a Competencia</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : competenciaEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="competencia-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="competencia-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="codigoLabel" for="competencia-codigo">
                  <Translate contentKey="oGestorApp.competencia.codigo">Codigo</Translate>
                </Label>
                <AvField id="competencia-codigo" type="string" className="form-control" name="codigo" />
              </AvGroup>
              <AvGroup>
                <Label id="nomeLabel" for="competencia-nome">
                  <Translate contentKey="oGestorApp.competencia.nome">Nome</Translate>
                </Label>
                <AvField id="competencia-nome" type="text" name="nome" />
              </AvGroup>
              <AvGroup>
                <Label id="descricaoLabel" for="competencia-descricao">
                  <Translate contentKey="oGestorApp.competencia.descricao">Descricao</Translate>
                </Label>
                <AvField id="competencia-descricao" type="text" name="descricao" />
              </AvGroup>
              <AvGroup>
                <Label for="competencia-empregados">
                  <Translate contentKey="oGestorApp.competencia.empregados">Empregados</Translate>
                </Label>
                <AvInput
                  id="competencia-empregados"
                  type="select"
                  multiple
                  className="form-control"
                  name="empregados"
                  value={competenciaEntity.empregados && competenciaEntity.empregados.map(e => e.id)}
                >
                  <option value="" key="0" />
                  {empregados
                    ? empregados.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.nome}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/competencia" replace color="info">
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
  competenciaEntity: storeState.competencia.entity,
  loading: storeState.competencia.loading,
  updating: storeState.competencia.updating,
  updateSuccess: storeState.competencia.updateSuccess
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

export default connect(mapStateToProps, mapDispatchToProps)(CompetenciaUpdate);
