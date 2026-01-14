import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './subject.reducer';

export const SubjectDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const subjectEntity = useAppSelector(state => state.subject.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="subjectDetailsHeading">
          <Translate contentKey="adaptiveQuizzApp.subject.detail.title">Subject</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{subjectEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="adaptiveQuizzApp.subject.name">Name</Translate>
            </span>
          </dt>
          <dd>{subjectEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="adaptiveQuizzApp.subject.description">Description</Translate>
            </span>
          </dt>
          <dd>{subjectEntity.description}</dd>
        </dl>
        <Button tag={Link} to="/subject" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/subject/${subjectEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SubjectDetail;
