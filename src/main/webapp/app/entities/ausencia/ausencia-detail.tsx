import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './ausencia.reducer';
import { IAusencia } from 'app/shared/model/ausencia.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAusenciaDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AusenciaDetail = (props: IAusenciaDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { ausenciaEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="oGestorApp.ausencia.detail.title">Ausencia</Translate> [<b>{ausenciaEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="tipo">
              <Translate contentKey="oGestorApp.ausencia.tipo">Tipo</Translate>
            </span>
          </dt>
          <dd>{ausenciaEntity.tipo}</dd>
          <dt>
            <span id="descricao">
              <Translate contentKey="oGestorApp.ausencia.descricao">Descricao</Translate>
            </span>
          </dt>
          <dd>{ausenciaEntity.descricao}</dd>
          <dt>
            <span id="dataInicio">
              <Translate contentKey="oGestorApp.ausencia.dataInicio">Data Inicio</Translate>
            </span>
          </dt>
          <dd>{ausenciaEntity.dataInicio}</dd>
          <dt>
            <span id="dataFim">
              <Translate contentKey="oGestorApp.ausencia.dataFim">Data Fim</Translate>
            </span>
          </dt>
          <dd>{ausenciaEntity.dataFim}</dd>
          <dt>
            <Translate contentKey="oGestorApp.ausencia.empregado">Empregado</Translate>
          </dt>
          <dd>{ausenciaEntity.empregado ? ausenciaEntity.empregado.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/ausencia" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ausencia/${ausenciaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ ausencia }: IRootState) => ({
  ausenciaEntity: ausencia.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AusenciaDetail);
