import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './lotacao.reducer';
import { ILotacao } from 'app/shared/model/lotacao.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ILotacaoProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const Lotacao = (props: ILotacaoProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { lotacaoList, match, loading } = props;
  return (
    <div>
      <h2 id="lotacao-heading">
        <Translate contentKey="oGestorApp.lotacao.home.title">Lotacaos</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="oGestorApp.lotacao.home.createLabel">Create new Lotacao</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {lotacaoList && lotacaoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="oGestorApp.lotacao.codigo">Codigo</Translate>
                </th>
                <th>
                  <Translate contentKey="oGestorApp.lotacao.nome">Nome</Translate>
                </th>
                <th>
                  <Translate contentKey="oGestorApp.lotacao.descricao">Descricao</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {lotacaoList.map((lotacao, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button hidden="true" tag={Link} to={`${match.url}/${lotacao.id}`} color="link" size="sm">
                      {lotacao.id}
                    </Button>
                  </td>
                  <td>{lotacao.codigo}</td>
                  <td>{lotacao.nome}</td>
                  <td>{lotacao.descricao}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${lotacao.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${lotacao.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${lotacao.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="oGestorApp.lotacao.home.notFound">No Lotacaos found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ lotacao }: IRootState) => ({
  lotacaoList: lotacao.entities,
  loading: lotacao.loading
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Lotacao);
