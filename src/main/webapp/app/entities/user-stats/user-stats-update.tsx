import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntities as getSubjects } from 'app/entities/subject/subject.reducer';
import { createEntity, getEntity, reset, updateEntity } from './user-stats.reducer';

export const UserStatsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const subjects = useAppSelector(state => state.subject.entities);
  const userStatsEntity = useAppSelector(state => state.userStats.entity);
  const loading = useAppSelector(state => state.userStats.loading);
  const updating = useAppSelector(state => state.userStats.updating);
  const updateSuccess = useAppSelector(state => state.userStats.updateSuccess);

  const handleClose = () => {
    navigate('/user-stats');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getSubjects({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.currentDifficulty !== undefined && typeof values.currentDifficulty !== 'number') {
      values.currentDifficulty = Number(values.currentDifficulty);
    }
    if (values.totalAnswered !== undefined && typeof values.totalAnswered !== 'number') {
      values.totalAnswered = Number(values.totalAnswered);
    }
    if (values.totalCorrect !== undefined && typeof values.totalCorrect !== 'number') {
      values.totalCorrect = Number(values.totalCorrect);
    }
    if (values.streakDays !== undefined && typeof values.streakDays !== 'number') {
      values.streakDays = Number(values.streakDays);
    }
    values.lastActiveAt = convertDateTimeToServer(values.lastActiveAt);

    const entity = {
      ...userStatsEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user?.toString()),
      subject: subjects.find(it => it.id.toString() === values.subject?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          lastActiveAt: displayDefaultDateTime(),
        }
      : {
          ...userStatsEntity,
          lastActiveAt: convertDateTimeFromServer(userStatsEntity.lastActiveAt),
          user: userStatsEntity?.user?.id,
          subject: userStatsEntity?.subject?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adaptiveQuizzApp.userStats.home.createOrEditLabel" data-cy="UserStatsCreateUpdateHeading">
            <Translate contentKey="adaptiveQuizzApp.userStats.home.createOrEditLabel">Create or edit a UserStats</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="user-stats-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adaptiveQuizzApp.userStats.currentDifficulty')}
                id="user-stats-currentDifficulty"
                name="currentDifficulty"
                data-cy="currentDifficulty"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 1, message: translate('entity.validation.min', { min: 1 }) },
                  max: { value: 5, message: translate('entity.validation.max', { max: 5 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('adaptiveQuizzApp.userStats.totalAnswered')}
                id="user-stats-totalAnswered"
                name="totalAnswered"
                data-cy="totalAnswered"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('adaptiveQuizzApp.userStats.totalCorrect')}
                id="user-stats-totalCorrect"
                name="totalCorrect"
                data-cy="totalCorrect"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('adaptiveQuizzApp.userStats.streakDays')}
                id="user-stats-streakDays"
                name="streakDays"
                data-cy="streakDays"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('adaptiveQuizzApp.userStats.lastActiveAt')}
                id="user-stats-lastActiveAt"
                name="lastActiveAt"
                data-cy="lastActiveAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="user-stats-user"
                name="user"
                data-cy="user"
                label={translate('adaptiveQuizzApp.userStats.user')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="user-stats-subject"
                name="subject"
                data-cy="subject"
                label={translate('adaptiveQuizzApp.userStats.subject')}
                type="select"
              >
                <option value="" key="0" />
                {subjects
                  ? subjects.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-stats" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default UserStatsUpdate;
