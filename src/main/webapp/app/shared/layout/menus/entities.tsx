import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Translate, translate } from 'react-jhipster';
import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <MenuItem icon="user" to="/empregado">
      <Translate contentKey="global.menu.entities.empregado" />
    </MenuItem>
    <MenuItem icon="book" to="/competencia">
      <Translate contentKey="global.menu.entities.competencia" />
    </MenuItem>
    <MenuItem icon="home" to="/lotacao">
      <Translate contentKey="global.menu.entities.lotacao" />
    </MenuItem>
    <MenuItem icon="tasks" to="/projeto">
      <Translate contentKey="global.menu.entities.projeto" />
    </MenuItem>
    <MenuItem icon="search" to="/ausencia">
      <Translate contentKey="global.menu.entities.ausencia" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
