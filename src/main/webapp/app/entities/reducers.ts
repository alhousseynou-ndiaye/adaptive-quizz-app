import subject from 'app/entities/subject/subject.reducer';
import questionType from 'app/entities/question-type/question-type.reducer';
import question from 'app/entities/question/question.reducer';
import answer from 'app/entities/answer/answer.reducer';
import reviewReminder from 'app/entities/review-reminder/review-reminder.reducer';
import userStats from 'app/entities/user-stats/user-stats.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  subject,
  questionType,
  question,
  answer,
  reviewReminder,
  userStats,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
