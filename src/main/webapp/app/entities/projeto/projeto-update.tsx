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
import { getEntity, updateEntity, createEntity, reset } from './projeto.reducer';
import { IProjeto } from 'app/shared/model/projeto.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IProjetoUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProjetoUpdate = (props: IProjetoUpdateProps) => {
  const [idsempregados, setIdsempregados] = useState([]);
  const [gestorId, setGestorId] = useState('0');
  const [listaempregadoId, setListaempregadoId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { projetoEntity, empregados, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/projeto');
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
        ...projetoEntity,
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
          <h2 id="oGestorApp.projeto.home.createOrEditLabel">
            <Translate contentKey="oGestorApp.projeto.home.createOrEditLabel">Create or edit a Projeto</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : projetoEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="projeto-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="projeto-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nomeLabel" for="projeto-nome">
                  <Translate contentKey="oGestorApp.projeto.nome">Nome</Translate>
                </Label>
                <AvField id="projeto-nome" type="text" name="nome" />
              </AvGroup>
              <AvGroup>
                <Label id="descricaoLabel" for="projeto-descricao">
                  <Translate contentKey="oGestorApp.projeto.descricao">Descricao</Translate>
                </Label>
                <AvField id="projeto-descricao" type="text" name="descricao" />
              </AvGroup>
              <AvGroup>
                <Label id="dataInicioLabel" for="projeto-dataInicio">
                  <Translate contentKey="oGestorApp.projeto.dataInicio">Data Inicio</Translate>
                </Label>
                <AvField id="projeto-dataInicio" type="date" name="dataInicio" />
              </AvGroup>
              <AvGroup>
                <Label id="dataFimLabel" for="projeto-dataFim">
                  <Translate contentKey="oGestorApp.projeto.dataFim">Data Fim</Translate>
                </Label>
                <AvField id="projeto-dataFim" type="date" name="dataFim" />
              </AvGroup>
              <AvGroup>
                <Label for="projeto-gestor">
                  <Translate contentKey="oGestorApp.projeto.gestor">Gestor</Translate>
                </Label>
                <AvInput id="projeto-gestor" type="select" className="form-control" name="gestor.id">
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
              <AvGroup>
                <Label for="projeto-empregados">
                  <Translate contentKey="oGestorApp.projeto.empregados">Empregados</Translate>
                </Label>
                <AvInput
                  id="projeto-empregados"
                  type="select"
                  multiple
                  className="form-control"
                  name="empregados"
                  value={projetoEntity.empregados && projetoEntity.empregados.map(e => e.id)}
                >
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
              <Button tag={Link} id="cancel-save" to="/projeto" replace color="info">
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
  projetoEntity: storeState.projeto.entity,
  loading: storeState.projeto.loading,
  updating: storeState.projeto.updating,
  updateSuccess: storeState.projeto.updateSuccess
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

export default connect(mapStateToProps, mapDispatchToProps)(ProjetoUpdate);
