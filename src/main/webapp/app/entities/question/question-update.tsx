import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getSubjects } from 'app/entities/subject/subject.reducer';
import { getEntities as getQuestionTypes } from 'app/entities/question-type/question-type.reducer';
import { createEntity, getEntity, reset, updateEntity } from './question.reducer';

export const QuestionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const subjects = useAppSelector(state => state.subject.entities);
  const questionTypes = useAppSelector(state => state.questionType.entities);
  const questionEntity = useAppSelector(state => state.question.entity);
  const loading = useAppSelector(state => state.question.loading);
  const updating = useAppSelector(state => state.question.updating);
  const updateSuccess = useAppSelector(state => state.question.updateSuccess);

  const handleClose = () => {
    navigate(`/question${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getSubjects({}));
    dispatch(getQuestionTypes({}));
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
    if (values.difficulty !== undefined && typeof values.difficulty !== 'number') {
      values.difficulty = Number(values.difficulty);
    }

    const entity = {
      ...questionEntity,
      ...values,
      subject: subjects.find(it => it.id.toString() === values.subject?.toString()),
      type: questionTypes.find(it => it.id.toString() === values.type?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...questionEntity,
          subject: questionEntity?.subject?.id,
          type: questionEntity?.type?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adaptiveQuizzApp.question.home.createOrEditLabel" data-cy="QuestionCreateUpdateHeading">
            <Translate contentKey="adaptiveQuizzApp.question.home.createOrEditLabel">Create or edit a Question</Translate>
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
                  id="question-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adaptiveQuizzApp.question.prompt')}
                id="question-prompt"
                name="prompt"
                data-cy="prompt"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adaptiveQuizzApp.question.difficulty')}
                id="question-difficulty"
                name="difficulty"
                data-cy="difficulty"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 1, message: translate('entity.validation.min', { min: 1 }) },
                  max: { value: 5, message: translate('entity.validation.max', { max: 5 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('adaptiveQuizzApp.question.explanation')}
                id="question-explanation"
                name="explanation"
                data-cy="explanation"
                type="textarea"
              />
              <ValidatedField
                label={translate('adaptiveQuizzApp.question.active')}
                id="question-active"
                name="active"
                data-cy="active"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adaptiveQuizzApp.question.choiceA')}
                id="question-choiceA"
                name="choiceA"
                data-cy="choiceA"
                type="text"
                validate={{
                  maxLength: { value: 500, message: translate('entity.validation.maxlength', { max: 500 }) },
                }}
              />
              <ValidatedField
                label={translate('adaptiveQuizzApp.question.choiceB')}
                id="question-choiceB"
                name="choiceB"
                data-cy="choiceB"
                type="text"
                validate={{
                  maxLength: { value: 500, message: translate('entity.validation.maxlength', { max: 500 }) },
                }}
              />
              <ValidatedField
                label={translate('adaptiveQuizzApp.question.choiceC')}
                id="question-choiceC"
                name="choiceC"
                data-cy="choiceC"
                type="text"
                validate={{
                  maxLength: { value: 500, message: translate('entity.validation.maxlength', { max: 500 }) },
                }}
              />
              <ValidatedField
                label={translate('adaptiveQuizzApp.question.choiceD')}
                id="question-choiceD"
                name="choiceD"
                data-cy="choiceD"
                type="text"
                validate={{
                  maxLength: { value: 500, message: translate('entity.validation.maxlength', { max: 500 }) },
                }}
              />
              <ValidatedField
                label={translate('adaptiveQuizzApp.question.correctChoice')}
                id="question-correctChoice"
                name="correctChoice"
                data-cy="correctChoice"
                type="text"
                validate={{
                  pattern: { value: /^[ABCD]$/, message: translate('entity.validation.pattern', { pattern: '^[ABCD]$' }) },
                }}
              />
              <ValidatedField
                label={translate('adaptiveQuizzApp.question.correctText')}
                id="question-correctText"
                name="correctText"
                data-cy="correctText"
                type="textarea"
              />
              <ValidatedField
                id="question-subject"
                name="subject"
                data-cy="subject"
                label={translate('adaptiveQuizzApp.question.subject')}
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
              <ValidatedField
                id="question-type"
                name="type"
                data-cy="type"
                label={translate('adaptiveQuizzApp.question.type')}
                type="select"
              >
                <option value="" key="0" />
                {questionTypes
                  ? questionTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.label}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/question" replace color="info">
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

export default QuestionUpdate;
