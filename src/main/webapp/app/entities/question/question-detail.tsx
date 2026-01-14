import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './question.reducer';

export const QuestionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const questionEntity = useAppSelector(state => state.question.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="questionDetailsHeading">
          <Translate contentKey="adaptiveQuizzApp.question.detail.title">Question</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{questionEntity.id}</dd>
          <dt>
            <span id="prompt">
              <Translate contentKey="adaptiveQuizzApp.question.prompt">Prompt</Translate>
            </span>
          </dt>
          <dd>{questionEntity.prompt}</dd>
          <dt>
            <span id="difficulty">
              <Translate contentKey="adaptiveQuizzApp.question.difficulty">Difficulty</Translate>
            </span>
          </dt>
          <dd>{questionEntity.difficulty}</dd>
          <dt>
            <span id="explanation">
              <Translate contentKey="adaptiveQuizzApp.question.explanation">Explanation</Translate>
            </span>
          </dt>
          <dd>{questionEntity.explanation}</dd>
          <dt>
            <span id="active">
              <Translate contentKey="adaptiveQuizzApp.question.active">Active</Translate>
            </span>
          </dt>
          <dd>{questionEntity.active ? 'true' : 'false'}</dd>
          <dt>
            <span id="choiceA">
              <Translate contentKey="adaptiveQuizzApp.question.choiceA">Choice A</Translate>
            </span>
          </dt>
          <dd>{questionEntity.choiceA}</dd>
          <dt>
            <span id="choiceB">
              <Translate contentKey="adaptiveQuizzApp.question.choiceB">Choice B</Translate>
            </span>
          </dt>
          <dd>{questionEntity.choiceB}</dd>
          <dt>
            <span id="choiceC">
              <Translate contentKey="adaptiveQuizzApp.question.choiceC">Choice C</Translate>
            </span>
          </dt>
          <dd>{questionEntity.choiceC}</dd>
          <dt>
            <span id="choiceD">
              <Translate contentKey="adaptiveQuizzApp.question.choiceD">Choice D</Translate>
            </span>
          </dt>
          <dd>{questionEntity.choiceD}</dd>
          <dt>
            <span id="correctChoice">
              <Translate contentKey="adaptiveQuizzApp.question.correctChoice">Correct Choice</Translate>
            </span>
          </dt>
          <dd>{questionEntity.correctChoice}</dd>
          <dt>
            <span id="correctText">
              <Translate contentKey="adaptiveQuizzApp.question.correctText">Correct Text</Translate>
            </span>
          </dt>
          <dd>{questionEntity.correctText}</dd>
          <dt>
            <Translate contentKey="adaptiveQuizzApp.question.subject">Subject</Translate>
          </dt>
          <dd>{questionEntity.subject ? questionEntity.subject.name : ''}</dd>
          <dt>
            <Translate contentKey="adaptiveQuizzApp.question.type">Type</Translate>
          </dt>
          <dd>{questionEntity.type ? questionEntity.type.label : ''}</dd>
        </dl>
        <Button tag={Link} to="/question" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/question/${questionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default QuestionDetail;
