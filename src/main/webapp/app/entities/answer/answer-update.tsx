import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntities as getQuestions } from 'app/entities/question/question.reducer';
import { createEntity, getEntity, reset, updateEntity } from './answer.reducer';

export const AnswerUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const questions = useAppSelector(state => state.question.entities);
  const answerEntity = useAppSelector(state => state.answer.entity);
  const loading = useAppSelector(state => state.answer.loading);
  const updating = useAppSelector(state => state.answer.updating);
  const updateSuccess = useAppSelector(state => state.answer.updateSuccess);

  const handleClose = () => {
    navigate(`/answer${location.search}`);
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
    values.answeredAt = convertDateTimeToServer(values.answeredAt);
    if (values.timeSpentMs !== undefined && typeof values.timeSpentMs !== 'number') {
      values.timeSpentMs = Number(values.timeSpentMs);
    }
    if (values.selfReportedRecall !== undefined && typeof values.selfReportedRecall !== 'number') {
      values.selfReportedRecall = Number(values.selfReportedRecall);
    }
    if (values.difficultyAtAnswer !== undefined && typeof values.difficultyAtAnswer !== 'number') {
      values.difficultyAtAnswer = Number(values.difficultyAtAnswer);
    }

    const entity = {
      ...answerEntity,
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
          answeredAt: displayDefaultDateTime(),
        }
      : {
          ...answerEntity,
          answeredAt: convertDateTimeFromServer(answerEntity.answeredAt),
          user: answerEntity?.user?.id,
          question: answerEntity?.question?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adaptiveQuizzApp.answer.home.createOrEditLabel" data-cy="AnswerCreateUpdateHeading">
            <Translate contentKey="adaptiveQuizzApp.answer.home.createOrEditLabel">Create or edit a Answer</Translate>
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
                  id="answer-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adaptiveQuizzApp.answer.answeredAt')}
                id="answer-answeredAt"
                name="answeredAt"
                data-cy="answeredAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adaptiveQuizzApp.answer.isCorrect')}
                id="answer-isCorrect"
                name="isCorrect"
                data-cy="isCorrect"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adaptiveQuizzApp.answer.timeSpentMs')}
                id="answer-timeSpentMs"
                name="timeSpentMs"
                data-cy="timeSpentMs"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('adaptiveQuizzApp.answer.selfReportedRecall')}
                id="answer-selfReportedRecall"
                name="selfReportedRecall"
                data-cy="selfReportedRecall"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  max: { value: 2, message: translate('entity.validation.max', { max: 2 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('adaptiveQuizzApp.answer.difficultyAtAnswer')}
                id="answer-difficultyAtAnswer"
                name="difficultyAtAnswer"
                data-cy="difficultyAtAnswer"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 1, message: translate('entity.validation.min', { min: 1 }) },
                  max: { value: 5, message: translate('entity.validation.max', { max: 5 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('adaptiveQuizzApp.answer.selectedChoice')}
                id="answer-selectedChoice"
                name="selectedChoice"
                data-cy="selectedChoice"
                type="text"
                validate={{
                  pattern: { value: /^[ABCD]$/, message: translate('entity.validation.pattern', { pattern: '^[ABCD]$' }) },
                }}
              />
              <ValidatedField
                label={translate('adaptiveQuizzApp.answer.freeTextAnswer')}
                id="answer-freeTextAnswer"
                name="freeTextAnswer"
                data-cy="freeTextAnswer"
                type="textarea"
              />
              <ValidatedField id="answer-user" name="user" data-cy="user" label={translate('adaptiveQuizzApp.answer.user')} type="select">
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
                id="answer-question"
                name="question"
                data-cy="question"
                label={translate('adaptiveQuizzApp.answer.question')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/answer" replace color="info">
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

export default AnswerUpdate;
