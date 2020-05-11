import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './competencia.reducer';
import { ICompetencia } from 'app/shared/model/competencia.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICompetenciaProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const Competencia = (props: ICompetenciaProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { competenciaList, match, loading } = props;
  return (
    <div>
      <h2 id="competencia-heading">
        <Translate contentKey="oGestorApp.competencia.home.title">Competencias</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="oGestorApp.competencia.home.createLabel">Create new Competencia</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {competenciaList && competenciaList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="oGestorApp.competencia.codigo">Codigo</Translate>
                </th>
                <th>
                  <Translate contentKey="oGestorApp.competencia.nome">Nome</Translate>
                </th>
                <th>
                  <Translate contentKey="oGestorApp.competencia.descricao">Descricao</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {competenciaList.map((competencia, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button hidden="true" tag={Link} to={`${match.url}/${competencia.id}`} color="link" size="sm">
                      {competencia.id}
                    </Button>
                  </td>
                  <td>{competencia.codigo}</td>
                  <td>{competencia.nome}</td>
                  <td>{competencia.descricao}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${competencia.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${competencia.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${competencia.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
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
              <Translate contentKey="oGestorApp.competencia.home.notFound">No Competencias found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ competencia }: IRootState) => ({
  competenciaList: competencia.entities,
  loading: competencia.loading
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Competencia);
