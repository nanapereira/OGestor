import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './ausencia.reducer';
import { IAusencia } from 'app/shared/model/ausencia.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import empregado from '../empregado/empregado';
import { Empregado } from '../empregado/empregado';
import { AvInput } from 'availity-reactstrap-validation';
import Entities from 'app/entities';
import { getEntity, updateEntity, createEntity, reset } from './ausencia.reducer';
import axios from 'axios';
import { ICrudGetAction } from 'react-jhipster';
import DocsPage from '../../modules/administration/docs/docs';

export interface IAusenciaProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> { }

export const Ausencia = (props: IAusenciaProps) => {

  const { ausenciaList, match, loading } = props;

  useEffect(() => {
    props.getEntities();
  }, []);

  return (
    <div>
      <h2 id="ausencia-heading">
        <Translate contentKey="oGestorApp.ausencia.home.title">Ausencias</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="oGestorApp.ausencia.home.createLabel">Create new Ausencia</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {ausenciaList && ausenciaList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="oGestorApp.ausencia.emp.matricula">Matricula</Translate>
                </th>
                <th>
                  <Translate contentKey="oGestorApp.ausencia.empregado">Empregado</Translate>
                </th>
                <th>
                  <Translate contentKey="oGestorApp.ausencia.tipo">Tipo</Translate>
                </th>
                <th>
                  <Translate contentKey="oGestorApp.ausencia.descricao">Descricao</Translate>
                </th>
                <th>
                  <Translate contentKey="oGestorApp.ausencia.dataInicio">Data Inicio</Translate>
                </th>
                <th>
                  <Translate contentKey="oGestorApp.ausencia.dataFim">Data Fim</Translate>
                </th>
                  <th />
              </tr>
            </thead>
            <tbody>
              {ausenciaList.map((ausencia, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button hidden="true" tag={Link} to={`${match.url}/${ausencia.id}`} color="link" size="sm">
                      {ausencia.id}
                    </Button>
                  </td>
                  <td>{ausencia.empregado ? <Link to={`empregado/${ausencia.empregado.id}`}>{ausencia.empregado.matricula}</Link> : ''}</td>
                  <td>{ausencia.empregado ? <Link to={`empregado/${ausencia.empregado.id}`}>{ausencia.empregado.nome}</Link> : ''}</td>
                  <td>
                    <Translate contentKey={`oGestorApp.TipoAusencia.${ausencia.tipo}`} />
                  </td>
                  <td>{ausencia.descricao}</td>
                  <td><TextFormat type="date" value={ausencia.dataInicio} format={APP_LOCAL_DATE_FORMAT} /></td>
                  <td><TextFormat type="date" value={ausencia.dataFim} format={APP_LOCAL_DATE_FORMAT} /></td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${ausencia.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${ausencia.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${ausencia.id}/delete`} color="danger" size="sm">
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
                <Translate contentKey="oGestorApp.ausencia.home.notFound">No Ausencias found</Translate>
              </div>
            )
          )}
      </div>
    </div>
  );
};

// eslint-disable-next-line no-shadow
const mapStateToProps = ({ ausencia }: IRootState) => ({
  ausenciaList: ausencia.entities,
  loading: ausencia.loading
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Ausencia);
