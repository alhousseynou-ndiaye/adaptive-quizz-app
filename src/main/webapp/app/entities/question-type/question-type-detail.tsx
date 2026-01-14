import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './question-type.reducer';

export const QuestionTypeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const questionTypeEntity = useAppSelector(state => state.questionType.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="questionTypeDetailsHeading">
          <Translate contentKey="adaptiveQuizzApp.questionType.detail.title">QuestionType</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{questionTypeEntity.id}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="adaptiveQuizzApp.questionType.code">Code</Translate>
            </span>
          </dt>
          <dd>{questionTypeEntity.code}</dd>
          <dt>
            <span id="label">
              <Translate contentKey="adaptiveQuizzApp.questionType.label">Label</Translate>
            </span>
          </dt>
          <dd>{questionTypeEntity.label}</dd>
        </dl>
        <Button tag={Link} to="/question-type" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/question-type/${questionTypeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default QuestionTypeDetail;
