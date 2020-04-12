import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Empregado from './empregado';
import EmpregadoDetail from './empregado-detail';
import EmpregadoUpdate from './empregado-update';
import EmpregadoDeleteDialog from './empregado-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={EmpregadoDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={EmpregadoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={EmpregadoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={EmpregadoDetail} />
      <ErrorBoundaryRoute path={match.url} component={Empregado} />
    </Switch>
  </>
);

export default Routes;
