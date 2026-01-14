import React from 'react';
import { Route } from 'react-router'; // eslint-disable-line

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Subject from './subject';
import QuestionType from './question-type';
import Question from './question';
import Answer from './answer';
import ReviewReminder from './review-reminder';
import UserStats from './user-stats';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="subject/*" element={<Subject />} />
        <Route path="question-type/*" element={<QuestionType />} />
        <Route path="question/*" element={<Question />} />
        <Route path="answer/*" element={<Answer />} />
        <Route path="review-reminder/*" element={<ReviewReminder />} />
        <Route path="user-stats/*" element={<UserStats />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
