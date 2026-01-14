import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntities as getQuestions } from 'app/entities/question/question.reducer';
import { ReminderStatus } from 'app/shared/model/enumerations/reminder-status.model';
import { ReminderKind } from 'app/shared/model/enumerations/reminder-kind.model';
import { createEntity, getEntity, reset, updateEntity } from './review-reminder.reducer';

export const ReviewReminderUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const questions = useAppSelector(state => state.question.entities);
  const reviewReminderEntity = useAppSelector(state => state.reviewReminder.entity);
  const loading = useAppSelector(state => state.reviewReminder.loading);
  const updating = useAppSelector(state => state.reviewReminder.updating);
  const updateSuccess = useAppSelector(state => state.reviewReminder.updateSuccess);
  const reminderStatusValues = Object.keys(ReminderStatus);
  const reminderKindValues = Object.keys(ReminderKind);

  const handleClose = () => {
    navigate(`/review-reminder${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getQuestions({}));
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
    values.dueAt = convertDateTimeToServer(values.dueAt);
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.doneAt = convertDateTimeToServer(values.doneAt);

    const entity = {
      ...reviewReminderEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user?.toString()),
      question: questions.find(it => it.id.toString() === values.question?.toString()),
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
          dueAt: displayDefaultDateTime(),
          createdAt: displayDefaultDateTime(),
          doneAt: displayDefaultDateTime(),
        }
      : {
          status: 'PENDING',
          kind: 'WEEK_1',
          ...reviewReminderEntity,
          dueAt: convertDateTimeFromServer(reviewReminderEntity.dueAt),
          createdAt: convertDateTimeFromServer(reviewReminderEntity.createdAt),
          doneAt: convertDateTimeFromServer(reviewReminderEntity.doneAt),
          user: reviewReminderEntity?.user?.id,
          question: reviewReminderEntity?.question?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adaptiveQuizzApp.reviewReminder.home.createOrEditLabel" data-cy="ReviewReminderCreateUpdateHeading">
            <Translate contentKey="adaptiveQuizzApp.reviewReminder.home.createOrEditLabel">Create or edit a ReviewReminder</Translate>
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
                  id="review-reminder-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adaptiveQuizzApp.reviewReminder.dueAt')}
                id="review-reminder-dueAt"
                name="dueAt"
                data-cy="dueAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adaptiveQuizzApp.reviewReminder.status')}
                id="review-reminder-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {reminderStatusValues.map(reminderStatus => (
                  <option value={reminderStatus} key={reminderStatus}>
                    {translate(`adaptiveQuizzApp.ReminderStatus.${reminderStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('adaptiveQuizzApp.reviewReminder.kind')}
                id="review-reminder-kind"
                name="kind"
                data-cy="kind"
                type="select"
              >
                {reminderKindValues.map(reminderKind => (
                  <option value={reminderKind} key={reminderKind}>
                    {translate(`adaptiveQuizzApp.ReminderKind.${reminderKind}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('adaptiveQuizzApp.reviewReminder.createdAt')}
                id="review-reminder-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adaptiveQuizzApp.reviewReminder.doneAt')}
                id="review-reminder-doneAt"
                name="doneAt"
                data-cy="doneAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="review-reminder-user"
                name="user"
                data-cy="user"
                label={translate('adaptiveQuizzApp.reviewReminder.user')}
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
                id="review-reminder-question"
                name="question"
                data-cy="question"
                label={translate('adaptiveQuizzApp.reviewReminder.question')}
                type="select"
              >
                <option value="" key="0" />
                {questions
                  ? questions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.prompt}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/review-reminder" replace color="info">
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

export default ReviewReminderUpdate;
