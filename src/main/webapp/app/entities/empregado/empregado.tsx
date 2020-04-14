import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './empregado.reducer';
import { IEmpregado } from 'app/shared/model/empregado.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IEmpregadoProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const Empregado = (props: IEmpregadoProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { empregadoList, match, loading } = props;
  return (
    <div>
      <h2 id="empregado-heading">
        <Translate contentKey="oGestorApp.empregado.home.title">Empregados</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="oGestorApp.empregado.home.createLabel">Create new Empregado</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {empregadoList && empregadoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="oGestorApp.empregado.matricula">Matricula</Translate>
                </th>
                <th>
                  <Translate contentKey="oGestorApp.empregado.nome">Nome</Translate>
                </th>
                <th>
                  <Translate contentKey="oGestorApp.empregado.cpf">Cpf</Translate>
                </th>
                <th>
                  <Translate contentKey="oGestorApp.empregado.email">Email</Translate>
                </th>
                <th>
                  <Translate contentKey="oGestorApp.empregado.telefone">Telefone</Translate>
                </th>
                <th>
                  <Translate contentKey="oGestorApp.empregado.ramal">Ramal</Translate>
                </th>
                <th>
                  <Translate contentKey="oGestorApp.empregado.lotacao">Lotacao</Translate>
                </th>
                <th>
                  <Translate contentKey="oGestorApp.empregado.competencias">Competencias</Translate>
                </th>
                <th>
                  <Translate contentKey="oGestorApp.empregado.projetos">Projetos</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {empregadoList.map((empregado, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button hidden="true" tag={Link} to={`${match.url}/${empregado.id}`} color="link" size="sm">
                      {empregado.id}
                    </Button>
                  </td>
                  <td>{empregado.matricula}</td>
                  <td>{empregado.nome}</td>
                  <td>{empregado.cpf}</td>
                  <td>{empregado.email}</td>
                  <td>{empregado.telefone}</td>
                  <td>{empregado.ramal}</td>
                  <td>{empregado.lotacao ? <Link to={`lotacao/${empregado.lotacao.id}`}>{empregado.lotacao.nome}</Link> : ''}</td>
                  <td>
                    {empregado.competencias
                      ? empregado.competencias.map((val, j) => (
                          <span key={j}>
                            <Link to={`competencia/${val.id}`}>{val.nome}</Link>
                            {j === empregado.competencias.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td>
                    {empregado.projetos
                      ? empregado.projetos.map((val, j) => (
                          <span key={j}>
                            <Link to={`projeto/${val.id}`}>{val.nome}</Link>
                            {j === empregado.projetos.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${empregado.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="search" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${empregado.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${empregado.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="oGestorApp.empregado.home.notFound">No Empregados found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ empregado }: IRootState) => ({
  empregadoList: empregado.entities,
  loading: empregado.loading
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Empregado);
