import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './projeto.reducer';
import { IProjeto } from 'app/shared/model/projeto.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProjetoDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProjetoDetail = (props: IProjetoDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { projetoEntity } = props;
  return (
    <Row>
      <Col md="6">
        <h3>
          <Translate contentKey="oGestorApp.projeto.detail.title">Projeto</Translate> <b>{projetoEntity.id}</b>
        </h3>
        <dl className="jh-entity-details">
          <dt>
            <span id="nome">
              <Translate contentKey="oGestorApp.projeto.nome">Nome</Translate>
            </span>
          </dt>
          <dd>{projetoEntity.nome}</dd>
          <dt>
            <span id="descricao">
              <Translate contentKey="oGestorApp.projeto.descricao">Descricao</Translate>
            </span>
          </dt>
          <dd>{projetoEntity.descricao}</dd>
          <dt>
            <span id="dataInicio">
              <Translate contentKey="oGestorApp.projeto.dataInicio">Data Inicio</Translate>
            </span>
          </dt>
          <dd>{projetoEntity.dataInicio}</dd>
          <dt>
            <span id="dataFim">
              <Translate contentKey="oGestorApp.projeto.dataFim">Data Fim</Translate>
            </span>
          </dt>
          <dd>{projetoEntity.dataFim}</dd>
          <dt>
            <Translate contentKey="oGestorApp.projeto.gestor">Gestor</Translate>
          </dt>
          <dd>{projetoEntity.gestor ? projetoEntity.gestor.nome : ''}</dd>
          <dt>
            <Translate contentKey="oGestorApp.projeto.empregados">Empregados</Translate>
          </dt>
          <dd>
            {projetoEntity.empregados
              ? projetoEntity.empregados.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.nome}</a>
                    {i === projetoEntity.empregados.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/projeto" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/projeto/${projetoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ projeto }: IRootState) => ({
  projetoEntity: projeto.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProjetoDetail);
