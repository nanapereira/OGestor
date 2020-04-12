import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Empregado from './empregado';
import Competencia from './competencia';
import Lotacao from './lotacao';
import Projeto from './projeto';
import Ausencia from './ausencia';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}empregado`} component={Empregado} />
      <ErrorBoundaryRoute path={`${match.url}competencia`} component={Competencia} />
      <ErrorBoundaryRoute path={`${match.url}lotacao`} component={Lotacao} />
      <ErrorBoundaryRoute path={`${match.url}projeto`} component={Projeto} />
      <ErrorBoundaryRoute path={`${match.url}ausencia`} component={Ausencia} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
