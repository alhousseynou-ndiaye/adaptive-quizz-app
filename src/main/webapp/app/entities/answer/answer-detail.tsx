import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './answer.reducer';

export const AnswerDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const answerEntity = useAppSelector(state => state.answer.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="answerDetailsHeading">
          <Translate contentKey="adaptiveQuizzApp.answer.detail.title">Answer</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{answerEntity.id}</dd>
          <dt>
            <span id="answeredAt">
              <Translate contentKey="adaptiveQuizzApp.answer.answeredAt">Answered At</Translate>
            </span>
          </dt>
          <dd>{answerEntity.answeredAt ? <TextFormat value={answerEntity.answeredAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="isCorrect">
              <Translate contentKey="adaptiveQuizzApp.answer.isCorrect">Is Correct</Translate>
            </span>
          </dt>
          <dd>{answerEntity.isCorrect ? 'true' : 'false'}</dd>
          <dt>
            <span id="timeSpentMs">
              <Translate contentKey="adaptiveQuizzApp.answer.timeSpentMs">Time Spent Ms</Translate>
            </span>
          </dt>
          <dd>{answerEntity.timeSpentMs}</dd>
          <dt>
            <span id="selfReportedRecall">
              <Translate contentKey="adaptiveQuizzApp.answer.selfReportedRecall">Self Reported Recall</Translate>
            </span>
          </dt>
          <dd>{answerEntity.selfReportedRecall}</dd>
          <dt>
            <span id="difficultyAtAnswer">
              <Translate contentKey="adaptiveQuizzApp.answer.difficultyAtAnswer">Difficulty At Answer</Translate>
            </span>
          </dt>
          <dd>{answerEntity.difficultyAtAnswer}</dd>
          <dt>
            <span id="selectedChoice">
              <Translate contentKey="adaptiveQuizzApp.answer.selectedChoice">Selected Choice</Translate>
            </span>
          </dt>
          <dd>{answerEntity.selectedChoice}</dd>
          <dt>
            <span id="freeTextAnswer">
              <Translate contentKey="adaptiveQuizzApp.answer.freeTextAnswer">Free Text Answer</Translate>
            </span>
          </dt>
          <dd>{answerEntity.freeTextAnswer}</dd>
          <dt>
            <Translate contentKey="adaptiveQuizzApp.answer.user">User</Translate>
          </dt>
          <dd>{answerEntity.user ? answerEntity.user.login : ''}</dd>
          <dt>
            <Translate contentKey="adaptiveQuizzApp.answer.question">Question</Translate>
          </dt>
          <dd>{answerEntity.question ? answerEntity.question.prompt : ''}</dd>
        </dl>
        <Button tag={Link} to="/answer" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/answer/${answerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AnswerDetail;
