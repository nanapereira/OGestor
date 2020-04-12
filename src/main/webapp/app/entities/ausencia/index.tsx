import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Ausencia from './ausencia';
import AusenciaDetail from './ausencia-detail';
import AusenciaUpdate from './ausencia-update';
import AusenciaDeleteDialog from './ausencia-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AusenciaDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AusenciaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AusenciaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AusenciaDetail} />
      <ErrorBoundaryRoute path={match.url} component={Ausencia} />
    </Switch>
  </>
);

export default Routes;
