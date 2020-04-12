import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Projeto from './projeto';
import ProjetoDetail from './projeto-detail';
import ProjetoUpdate from './projeto-update';
import ProjetoDeleteDialog from './projeto-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ProjetoDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProjetoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProjetoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProjetoDetail} />
      <ErrorBoundaryRoute path={match.url} component={Projeto} />
    </Switch>
  </>
);

export default Routes;
