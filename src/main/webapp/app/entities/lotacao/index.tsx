import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Lotacao from './lotacao';
import LotacaoDetail from './lotacao-detail';
import LotacaoUpdate from './lotacao-update';
import LotacaoDeleteDialog from './lotacao-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={LotacaoDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LotacaoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LotacaoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LotacaoDetail} />
      <ErrorBoundaryRoute path={match.url} component={Lotacao} />
    </Switch>
  </>
);

export default Routes;
