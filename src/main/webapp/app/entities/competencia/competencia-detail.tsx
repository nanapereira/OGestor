import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './competencia.reducer';
import { ICompetencia } from 'app/shared/model/competencia.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICompetenciaDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CompetenciaDetail = (props: ICompetenciaDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { competenciaEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="oGestorApp.competencia.detail.title">Competencia</Translate> <b>{competenciaEntity.id}</b>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="codigo">
              <Translate contentKey="oGestorApp.competencia.codigo">Codigo</Translate>
            </span>
          </dt>
          <dd>{competenciaEntity.codigo}</dd>
          <dt>
            <span id="nome">
              <Translate contentKey="oGestorApp.competencia.nome">Nome</Translate>
            </span>
          </dt>
          <dd>{competenciaEntity.nome}</dd>
          <dt>
            <span id="descricao">
              <Translate contentKey="oGestorApp.competencia.descricao">Descricao</Translate>
            </span>
          </dt>
          <dd>{competenciaEntity.descricao}</dd>
          <dt>
            <Translate contentKey="oGestorApp.competencia.empregados">Empregados</Translate>
          </dt>
          <dd>
            {competenciaEntity.empregados
              ? competenciaEntity.empregados.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.nome}</a>
                    {i === competenciaEntity.empregados.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/competencia" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/competencia/${competenciaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ competencia }: IRootState) => ({
  competenciaEntity: competencia.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CompetenciaDetail);
