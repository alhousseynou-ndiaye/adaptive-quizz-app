import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-stats.reducer';

export const UserStatsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userStatsEntity = useAppSelector(state => state.userStats.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userStatsDetailsHeading">
          <Translate contentKey="adaptiveQuizzApp.userStats.detail.title">UserStats</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userStatsEntity.id}</dd>
          <dt>
            <span id="currentDifficulty">
              <Translate contentKey="adaptiveQuizzApp.userStats.currentDifficulty">Current Difficulty</Translate>
            </span>
          </dt>
          <dd>{userStatsEntity.currentDifficulty}</dd>
          <dt>
            <span id="totalAnswered">
              <Translate contentKey="adaptiveQuizzApp.userStats.totalAnswered">Total Answered</Translate>
            </span>
          </dt>
          <dd>{userStatsEntity.totalAnswered}</dd>
          <dt>
            <span id="totalCorrect">
              <Translate contentKey="adaptiveQuizzApp.userStats.totalCorrect">Total Correct</Translate>
            </span>
          </dt>
          <dd>{userStatsEntity.totalCorrect}</dd>
          <dt>
            <span id="streakDays">
              <Translate contentKey="adaptiveQuizzApp.userStats.streakDays">Streak Days</Translate>
            </span>
          </dt>
          <dd>{userStatsEntity.streakDays}</dd>
          <dt>
            <span id="lastActiveAt">
              <Translate contentKey="adaptiveQuizzApp.userStats.lastActiveAt">Last Active At</Translate>
            </span>
          </dt>
          <dd>
            {userStatsEntity.lastActiveAt ? <TextFormat value={userStatsEntity.lastActiveAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="adaptiveQuizzApp.userStats.user">User</Translate>
          </dt>
          <dd>{userStatsEntity.user ? userStatsEntity.user.login : ''}</dd>
          <dt>
            <Translate contentKey="adaptiveQuizzApp.userStats.subject">Subject</Translate>
          </dt>
          <dd>{userStatsEntity.subject ? userStatsEntity.subject.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-stats" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-stats/${userStatsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserStatsDetail;
