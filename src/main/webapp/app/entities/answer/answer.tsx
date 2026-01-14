import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './answer.reducer';

export const Answer = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const answerList = useAppSelector(state => state.answer.entities);
  const loading = useAppSelector(state => state.answer.loading);
  const totalItems = useAppSelector(state => state.answer.totalItems);

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
      <h2 id="answer-heading" data-cy="AnswerHeading">
        <Translate contentKey="adaptiveQuizzApp.answer.home.title">Answers</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="adaptiveQuizzApp.answer.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/answer/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="adaptiveQuizzApp.answer.home.createLabel">Create new Answer</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {answerList && answerList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="adaptiveQuizzApp.answer.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('answeredAt')}>
                  <Translate contentKey="adaptiveQuizzApp.answer.answeredAt">Answered At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('answeredAt')} />
                </th>
                <th className="hand" onClick={sort('isCorrect')}>
                  <Translate contentKey="adaptiveQuizzApp.answer.isCorrect">Is Correct</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isCorrect')} />
                </th>
                <th className="hand" onClick={sort('timeSpentMs')}>
                  <Translate contentKey="adaptiveQuizzApp.answer.timeSpentMs">Time Spent Ms</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('timeSpentMs')} />
                </th>
                <th className="hand" onClick={sort('selfReportedRecall')}>
                  <Translate contentKey="adaptiveQuizzApp.answer.selfReportedRecall">Self Reported Recall</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('selfReportedRecall')} />
                </th>
                <th className="hand" onClick={sort('difficultyAtAnswer')}>
                  <Translate contentKey="adaptiveQuizzApp.answer.difficultyAtAnswer">Difficulty At Answer</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('difficultyAtAnswer')} />
                </th>
                <th className="hand" onClick={sort('selectedChoice')}>
                  <Translate contentKey="adaptiveQuizzApp.answer.selectedChoice">Selected Choice</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('selectedChoice')} />
                </th>
                <th className="hand" onClick={sort('freeTextAnswer')}>
                  <Translate contentKey="adaptiveQuizzApp.answer.freeTextAnswer">Free Text Answer</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('freeTextAnswer')} />
                </th>
                <th>
                  <Translate contentKey="adaptiveQuizzApp.answer.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="adaptiveQuizzApp.answer.question">Question</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {answerList.map((answer, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/answer/${answer.id}`} color="link" size="sm">
                      {answer.id}
                    </Button>
                  </td>
                  <td>{answer.answeredAt ? <TextFormat type="date" value={answer.answeredAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{answer.isCorrect ? 'true' : 'false'}</td>
                  <td>{answer.timeSpentMs}</td>
                  <td>{answer.selfReportedRecall}</td>
                  <td>{answer.difficultyAtAnswer}</td>
                  <td>{answer.selectedChoice}</td>
                  <td>{answer.freeTextAnswer}</td>
                  <td>{answer.user ? answer.user.login : ''}</td>
                  <td>{answer.question ? <Link to={`/question/${answer.question.id}`}>{answer.question.prompt}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/answer/${answer.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/answer/${answer.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/answer/${answer.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="adaptiveQuizzApp.answer.home.notFound">No Answers found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={answerList && answerList.length > 0 ? '' : 'd-none'}>
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

export default Answer;
