import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import QuestionType from './question-type';
import QuestionTypeDetail from './question-type-detail';
import QuestionTypeUpdate from './question-type-update';
import QuestionTypeDeleteDialog from './question-type-delete-dialog';

const QuestionTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<QuestionType />} />
    <Route path="new" element={<QuestionTypeUpdate />} />
    <Route path=":id">
      <Route index element={<QuestionTypeDetail />} />
      <Route path="edit" element={<QuestionTypeUpdate />} />
      <Route path="delete" element={<QuestionTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default QuestionTypeRoutes;
