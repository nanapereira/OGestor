import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ILotacao } from 'app/shared/model/lotacao.model';
import { getEntities as getLotacaos } from 'app/entities/lotacao/lotacao.reducer';
import { ICompetencia } from 'app/shared/model/competencia.model';
import { getEntities as getCompetencias } from 'app/entities/competencia/competencia.reducer';
import { IProjeto } from 'app/shared/model/projeto.model';
import { getEntities as getProjetos } from 'app/entities/projeto/projeto.reducer';
import { getEntity, updateEntity, createEntity, reset } from './empregado.reducer';
import { IEmpregado } from 'app/shared/model/empregado.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IEmpregadoUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const EmpregadoUpdate = (props: IEmpregadoUpdateProps) => {
  const [idscompetencias, setIdscompetencias] = useState([]);
  const [idsprojetos, setIdsprojetos] = useState([]);
  const [lotacaoId, setLotacaoId] = useState('0');
  const [listaCompetenciasId, setListaCompetenciasId] = useState('0');
  const [listaProjetosId, setListaProjetosId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { empregadoEntity, lotacaos, competencias, projetos, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/empregado');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getLotacaos();
    props.getCompetencias();
    props.getProjetos();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...empregadoEntity,
        ...values,
        competencias: mapIdList(values.competencias),
        projetos: mapIdList(values.projetos)
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
          <h2 id="oGestorApp.empregado.home.createOrEditLabel">
            <Translate contentKey="oGestorApp.empregado.home.createOrEditLabel">Create or edit a Empregado</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : empregadoEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="empregado-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="empregado-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="matriculaLabel" for="empregado-matricula">
                  <Translate contentKey="oGestorApp.empregado.matricula">Matricula</Translate>
                </Label>
                <AvField id="empregado-matricula" type="text" name="matricula" />
              </AvGroup>
              <AvGroup>
                <Label id="nomeLabel" for="empregado-nome">
                  <Translate contentKey="oGestorApp.empregado.nome">Nome</Translate>
                </Label>
                <AvField id="empregado-nome" type="text" name="nome" />
              </AvGroup>
              <AvGroup>
                <Label id="cpfLabel" for="empregado-cpf">
                  <Translate contentKey="oGestorApp.empregado.cpf">Cpf</Translate>
                </Label>
                <AvField id="empregado-cpf" type="text" name="cpf" />
              </AvGroup>
              <AvGroup>
                <Label id="emailLabel" for="empregado-email">
                  <Translate contentKey="oGestorApp.empregado.email">Email</Translate>
                </Label>
                <AvField id="empregado-email" type="text" name="email" />
              </AvGroup>
              <AvGroup>
                <Label id="telefoneLabel" for="empregado-telefone">
                  <Translate contentKey="oGestorApp.empregado.telefone">Telefone</Translate>
                </Label>
                <AvField id="empregado-telefone" type="text" name="telefone" />
              </AvGroup>
              <AvGroup>
                <Label id="ramalLabel" for="empregado-ramal">
                  <Translate contentKey="oGestorApp.empregado.ramal">Ramal</Translate>
                </Label>
                <AvField id="empregado-ramal" type="text" name="ramal" />
              </AvGroup>
              <AvGroup>
                <Label for="empregado-lotacao">
                  <Translate contentKey="oGestorApp.empregado.lotacao">Lotacao</Translate>
                </Label>
                <AvInput id="empregado-lotacao" type="select" className="form-control" name="lotacao.id">
                  <option value="" key="0" />
                  {lotacaos
                    ? lotacaos.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="empregado-competencias">
                  <Translate contentKey="oGestorApp.empregado.competencias">Competencias</Translate>
                </Label>
                <AvInput
                  id="empregado-competencias"
                  type="select"
                  multiple
                  className="form-control"
                  name="competencias"
                  value={empregadoEntity.competencias && empregadoEntity.competencias.map(e => e.id)}
                >
                  <option value="" key="0" />
                  {competencias
                    ? competencias.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="empregado-projetos">
                  <Translate contentKey="oGestorApp.empregado.projetos">Projetos</Translate>
                </Label>
                <AvInput
                  id="empregado-projetos"
                  type="select"
                  multiple
                  className="form-control"
                  name="projetos"
                  value={empregadoEntity.projetos && empregadoEntity.projetos.map(e => e.id)}
                >
                  <option value="" key="0" />
                  {projetos
                    ? projetos.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/empregado" replace color="info">
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
  lotacaos: storeState.lotacao.entities,
  competencias: storeState.competencia.entities,
  projetos: storeState.projeto.entities,
  empregadoEntity: storeState.empregado.entity,
  loading: storeState.empregado.loading,
  updating: storeState.empregado.updating,
  updateSuccess: storeState.empregado.updateSuccess
});

const mapDispatchToProps = {
  getLotacaos,
  getCompetencias,
  getProjetos,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(EmpregadoUpdate);
