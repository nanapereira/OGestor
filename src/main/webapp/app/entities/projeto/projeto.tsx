import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './projeto.reducer';
import { IProjeto } from 'app/shared/model/projeto.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProjetoProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const Projeto = (props: IProjetoProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { projetoList, match, loading } = props;
  return (
    <div>
      <h2 id="projeto-heading">
        <Translate contentKey="oGestorApp.projeto.home.title">Projetos</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="oGestorApp.projeto.home.createLabel">Create new Projeto</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {projetoList && projetoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="oGestorApp.projeto.nome">Nome</Translate>
                </th>
                <th>
                  <Translate contentKey="oGestorApp.projeto.descricao">Descricao</Translate>
                </th>
                <th>
                  <Translate contentKey="oGestorApp.projeto.dataInicio">Data Inicio</Translate>
                </th>
                <th>
                  <Translate contentKey="oGestorApp.projeto.dataFim">Data Fim</Translate>
                </th>
                <th>
                  <Translate contentKey="oGestorApp.projeto.gestor">Gestor</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {projetoList.map((projeto, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button hidden="true" tag={Link} to={`${match.url}/${projeto.id}`} color="link" size="sm">
                      {projeto.id}
                    </Button>
                  </td>
                  <td>{projeto.nome}</td>
                  <td>{projeto.descricao}</td>
                  <td><TextFormat type="date" value={projeto.dataInicio} format={APP_LOCAL_DATE_FORMAT} /></td>
                  <td><TextFormat type="date" value={projeto.dataFim} format={APP_LOCAL_DATE_FORMAT} /></td>
                  <td>{projeto.gestor ? <Link to={`empregado/${projeto.gestor.id}`}>{projeto.gestor.nome}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${projeto.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${projeto.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${projeto.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="oGestorApp.projeto.home.notFound">No Projetos found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ projeto }: IRootState) => ({
  projetoList: projeto.entities,
  loading: projeto.loading
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Projeto);
