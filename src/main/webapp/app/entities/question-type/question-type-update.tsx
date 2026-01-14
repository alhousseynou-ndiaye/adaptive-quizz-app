import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { QuestionTypeCode } from 'app/shared/model/enumerations/question-type-code.model';
import { createEntity, getEntity, reset, updateEntity } from './question-type.reducer';

export const QuestionTypeUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const questionTypeEntity = useAppSelector(state => state.questionType.entity);
  const loading = useAppSelector(state => state.questionType.loading);
  const updating = useAppSelector(state => state.questionType.updating);
  const updateSuccess = useAppSelector(state => state.questionType.updateSuccess);
  const questionTypeCodeValues = Object.keys(QuestionTypeCode);

  const handleClose = () => {
    navigate('/question-type');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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

    const entity = {
      ...questionTypeEntity,
      ...values,
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
          code: 'MCQ',
          ...questionTypeEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adaptiveQuizzApp.questionType.home.createOrEditLabel" data-cy="QuestionTypeCreateUpdateHeading">
            <Translate contentKey="adaptiveQuizzApp.questionType.home.createOrEditLabel">Create or edit a QuestionType</Translate>
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
                  id="question-type-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adaptiveQuizzApp.questionType.code')}
                id="question-type-code"
                name="code"
                data-cy="code"
                type="select"
              >
                {questionTypeCodeValues.map(questionTypeCode => (
                  <option value={questionTypeCode} key={questionTypeCode}>
                    {translate(`adaptiveQuizzApp.QuestionTypeCode.${questionTypeCode}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('adaptiveQuizzApp.questionType.label')}
                id="question-type-label"
                name="label"
                data-cy="label"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 2, message: translate('entity.validation.minlength', { min: 2 }) },
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/question-type" replace color="info">
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

export default QuestionTypeUpdate;
