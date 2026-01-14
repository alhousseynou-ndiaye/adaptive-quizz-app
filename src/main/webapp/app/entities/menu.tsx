import React from 'react';
import { Translate } from 'react-jhipster'; // eslint-disable-line

import MenuItem from 'app/shared/layout/menus/menu-item'; // eslint-disable-line

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/subject">
        <Translate contentKey="global.menu.entities.subject" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/question-type">
        <Translate contentKey="global.menu.entities.questionType" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/question">
        <Translate contentKey="global.menu.entities.question" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/answer">
        <Translate contentKey="global.menu.entities.answer" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/review-reminder">
        <Translate contentKey="global.menu.entities.reviewReminder" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/user-stats">
        <Translate contentKey="global.menu.entities.userStats" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
