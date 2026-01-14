import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './review-reminder.reducer';

export const ReviewReminderDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const reviewReminderEntity = useAppSelector(state => state.reviewReminder.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="reviewReminderDetailsHeading">
          <Translate contentKey="adaptiveQuizzApp.reviewReminder.detail.title">ReviewReminder</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{reviewReminderEntity.id}</dd>
          <dt>
            <span id="dueAt">
              <Translate contentKey="adaptiveQuizzApp.reviewReminder.dueAt">Due At</Translate>
            </span>
          </dt>
          <dd>
            {reviewReminderEntity.dueAt ? <TextFormat value={reviewReminderEntity.dueAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="status">
              <Translate contentKey="adaptiveQuizzApp.reviewReminder.status">Status</Translate>
            </span>
          </dt>
          <dd>{reviewReminderEntity.status}</dd>
          <dt>
            <span id="kind">
              <Translate contentKey="adaptiveQuizzApp.reviewReminder.kind">Kind</Translate>
            </span>
          </dt>
          <dd>{reviewReminderEntity.kind}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="adaptiveQuizzApp.reviewReminder.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {reviewReminderEntity.createdAt ? (
              <TextFormat value={reviewReminderEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="doneAt">
              <Translate contentKey="adaptiveQuizzApp.reviewReminder.doneAt">Done At</Translate>
            </span>
          </dt>
          <dd>
            {reviewReminderEntity.doneAt ? <TextFormat value={reviewReminderEntity.doneAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="adaptiveQuizzApp.reviewReminder.user">User</Translate>
          </dt>
          <dd>{reviewReminderEntity.user ? reviewReminderEntity.user.login : ''}</dd>
          <dt>
            <Translate contentKey="adaptiveQuizzApp.reviewReminder.question">Question</Translate>
          </dt>
          <dd>{reviewReminderEntity.question ? reviewReminderEntity.question.prompt : ''}</dd>
        </dl>
        <Button tag={Link} to="/review-reminder" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/review-reminder/${reviewReminderEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReviewReminderDetail;
