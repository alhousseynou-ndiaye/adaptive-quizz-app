import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserStats from './user-stats';
import UserStatsDetail from './user-stats-detail';
import UserStatsUpdate from './user-stats-update';
import UserStatsDeleteDialog from './user-stats-delete-dialog';

const UserStatsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserStats />} />
    <Route path="new" element={<UserStatsUpdate />} />
    <Route path=":id">
      <Route index element={<UserStatsDetail />} />
      <Route path="edit" element={<UserStatsUpdate />} />
      <Route path="delete" element={<UserStatsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserStatsRoutes;
