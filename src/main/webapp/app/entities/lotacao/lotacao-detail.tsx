import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './lotacao.reducer';
import { ILotacao } from 'app/shared/model/lotacao.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ILotacaoDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const LotacaoDetail = (props: ILotacaoDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { lotacaoEntity } = props;
  return (
    <Row>
      <Col md="6">
        <h4>
          <Translate contentKey="oGestorApp.lotacao.detail.title">Lotacao</Translate> <b>{lotacaoEntity.id}</b>
        </h4>
        <dl className="jh-entity-details">
          <dt>
            <span id="codigo">
              <Translate contentKey="oGestorApp.lotacao.codigo">Codigo</Translate>
            </span>
          </dt>
          <dd>{lotacaoEntity.codigo}</dd>
          <dt>
            <span id="nome">
              <Translate contentKey="oGestorApp.lotacao.nome">Nome</Translate>
            </span>
          </dt>
          <dd>{lotacaoEntity.nome}</dd>
          <dt>
            <span id="descricao">
              <Translate contentKey="oGestorApp.lotacao.descricao">Descricao</Translate>
            </span>
          </dt>
          <dd>{lotacaoEntity.descricao}</dd>
        </dl>
        <Button tag={Link} to="/lotacao" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/lotacao/${lotacaoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ lotacao }: IRootState) => ({
  lotacaoEntity: lotacao.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(LotacaoDetail);
