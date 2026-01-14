import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ReviewReminder from './review-reminder';
import ReviewReminderDetail from './review-reminder-detail';
import ReviewReminderUpdate from './review-reminder-update';
import ReviewReminderDeleteDialog from './review-reminder-delete-dialog';

const ReviewReminderRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ReviewReminder />} />
    <Route path="new" element={<ReviewReminderUpdate />} />
    <Route path=":id">
      <Route index element={<ReviewReminderDetail />} />
      <Route path="edit" element={<ReviewReminderUpdate />} />
      <Route path="delete" element={<ReviewReminderDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ReviewReminderRoutes;
