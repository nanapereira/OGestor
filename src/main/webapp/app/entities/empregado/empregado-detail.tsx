import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './empregado.reducer';
import { IEmpregado } from 'app/shared/model/empregado.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IEmpregadoDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const EmpregadoDetail = (props: IEmpregadoDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { empregadoEntity } = props;
  return (
    <Row>
      <Col md="6">
        <h4>
          <Translate contentKey="oGestorApp.empregado.detail.title">Empregado</Translate> <b>{empregadoEntity.id}</b>
        </h4>
        <dl className="jh-entity-details">
          <dt>
            <span id="matricula">
              <Translate contentKey="oGestorApp.empregado.matricula">Matricula</Translate>
            </span>
          </dt>
          <dd>{empregadoEntity.matricula}</dd>
          <dt>
            <span id="nome">
              <Translate contentKey="oGestorApp.empregado.nome">Nome</Translate>
            </span>
          </dt>
          <dd>{empregadoEntity.nome}</dd>
          <dt>
            <span id="cpf">
              <Translate contentKey="oGestorApp.empregado.cpf">Cpf</Translate>
            </span>
          </dt>
          <dd>{empregadoEntity.cpf}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="oGestorApp.empregado.email">Email</Translate>
            </span>
          </dt>
          <dd>{empregadoEntity.email}</dd>
          <dt>
            <span id="telefone">
              <Translate contentKey="oGestorApp.empregado.telefone">Telefone</Translate>
            </span>
          </dt>
          <dd>{empregadoEntity.telefone}</dd>
          <dt>
            <span id="ramal">
              <Translate contentKey="oGestorApp.empregado.ramal">Ramal</Translate>
            </span>
          </dt>
          <dd>{empregadoEntity.ramal}</dd>
          <dt>
            <Translate contentKey="oGestorApp.empregado.lotacao">Lotacao</Translate>
          </dt>
          <dd>{empregadoEntity.lotacao ? empregadoEntity.lotacao.nome : ''}</dd>
          <dt>
            <Translate contentKey="oGestorApp.empregado.competencias">Competencias</Translate>
          </dt>
          <dd>
            {empregadoEntity.competencias
              ? empregadoEntity.competencias.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.nome}</a>
                    {i === empregadoEntity.competencias.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="oGestorApp.empregado.projetos">Projetos</Translate>
          </dt>
          <dd>
            {empregadoEntity.projetos
              ? empregadoEntity.projetos.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.nome}</a>
                    {i === empregadoEntity.projetos.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/empregado" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/empregado/${empregadoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ empregado }: IRootState) => ({
  empregadoEntity: empregado.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(EmpregadoDetail);
