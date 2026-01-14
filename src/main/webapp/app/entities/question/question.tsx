import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './question.reducer';

export const Question = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const questionList = useAppSelector(state => state.question.entities);
  const loading = useAppSelector(state => state.question.loading);
  const totalItems = useAppSelector(state => state.question.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="question-heading" data-cy="QuestionHeading">
        <Translate contentKey="adaptiveQuizzApp.question.home.title">Questions</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="adaptiveQuizzApp.question.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/question/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="adaptiveQuizzApp.question.home.createLabel">Create new Question</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {questionList && questionList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="adaptiveQuizzApp.question.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('prompt')}>
                  <Translate contentKey="adaptiveQuizzApp.question.prompt">Prompt</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('prompt')} />
                </th>
                <th className="hand" onClick={sort('difficulty')}>
                  <Translate contentKey="adaptiveQuizzApp.question.difficulty">Difficulty</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('difficulty')} />
                </th>
                <th className="hand" onClick={sort('explanation')}>
                  <Translate contentKey="adaptiveQuizzApp.question.explanation">Explanation</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('explanation')} />
                </th>
                <th className="hand" onClick={sort('active')}>
                  <Translate contentKey="adaptiveQuizzApp.question.active">Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('active')} />
                </th>
                <th className="hand" onClick={sort('choiceA')}>
                  <Translate contentKey="adaptiveQuizzApp.question.choiceA">Choice A</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('choiceA')} />
                </th>
                <th className="hand" onClick={sort('choiceB')}>
                  <Translate contentKey="adaptiveQuizzApp.question.choiceB">Choice B</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('choiceB')} />
                </th>
                <th className="hand" onClick={sort('choiceC')}>
                  <Translate contentKey="adaptiveQuizzApp.question.choiceC">Choice C</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('choiceC')} />
                </th>
                <th className="hand" onClick={sort('choiceD')}>
                  <Translate contentKey="adaptiveQuizzApp.question.choiceD">Choice D</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('choiceD')} />
                </th>
                <th className="hand" onClick={sort('correctChoice')}>
                  <Translate contentKey="adaptiveQuizzApp.question.correctChoice">Correct Choice</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('correctChoice')} />
                </th>
                <th className="hand" onClick={sort('correctText')}>
                  <Translate contentKey="adaptiveQuizzApp.question.correctText">Correct Text</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('correctText')} />
                </th>
                <th>
                  <Translate contentKey="adaptiveQuizzApp.question.subject">Subject</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="adaptiveQuizzApp.question.type">Type</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {questionList.map((question, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/question/${question.id}`} color="link" size="sm">
                      {question.id}
                    </Button>
                  </td>
                  <td>{question.prompt}</td>
                  <td>{question.difficulty}</td>
                  <td>{question.explanation}</td>
                  <td>{question.active ? 'true' : 'false'}</td>
                  <td>{question.choiceA}</td>
                  <td>{question.choiceB}</td>
                  <td>{question.choiceC}</td>
                  <td>{question.choiceD}</td>
                  <td>{question.correctChoice}</td>
                  <td>{question.correctText}</td>
                  <td>{question.subject ? <Link to={`/subject/${question.subject.id}`}>{question.subject.name}</Link> : ''}</td>
                  <td>{question.type ? <Link to={`/question-type/${question.type.id}`}>{question.type.label}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/question/${question.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/question/${question.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/question/${question.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="adaptiveQuizzApp.question.home.notFound">No Questions found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={questionList && questionList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Question;
