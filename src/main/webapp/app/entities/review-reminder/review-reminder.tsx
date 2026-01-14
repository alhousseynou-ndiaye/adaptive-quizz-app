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

import { getEntities } from './review-reminder.reducer';

export const ReviewReminder = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const reviewReminderList = useAppSelector(state => state.reviewReminder.entities);
  const loading = useAppSelector(state => state.reviewReminder.loading);
  const totalItems = useAppSelector(state => state.reviewReminder.totalItems);

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
      <h2 id="review-reminder-heading" data-cy="ReviewReminderHeading">
        <Translate contentKey="adaptiveQuizzApp.reviewReminder.home.title">Review Reminders</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="adaptiveQuizzApp.reviewReminder.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/review-reminder/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="adaptiveQuizzApp.reviewReminder.home.createLabel">Create new Review Reminder</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {reviewReminderList && reviewReminderList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="adaptiveQuizzApp.reviewReminder.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('dueAt')}>
                  <Translate contentKey="adaptiveQuizzApp.reviewReminder.dueAt">Due At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dueAt')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="adaptiveQuizzApp.reviewReminder.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('kind')}>
                  <Translate contentKey="adaptiveQuizzApp.reviewReminder.kind">Kind</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('kind')} />
                </th>
                <th className="hand" onClick={sort('createdAt')}>
                  <Translate contentKey="adaptiveQuizzApp.reviewReminder.createdAt">Created At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdAt')} />
                </th>
                <th className="hand" onClick={sort('doneAt')}>
                  <Translate contentKey="adaptiveQuizzApp.reviewReminder.doneAt">Done At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('doneAt')} />
                </th>
                <th>
                  <Translate contentKey="adaptiveQuizzApp.reviewReminder.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="adaptiveQuizzApp.reviewReminder.question">Question</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {reviewReminderList.map((reviewReminder, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/review-reminder/${reviewReminder.id}`} color="link" size="sm">
                      {reviewReminder.id}
                    </Button>
                  </td>
                  <td>{reviewReminder.dueAt ? <TextFormat type="date" value={reviewReminder.dueAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    <Translate contentKey={`adaptiveQuizzApp.ReminderStatus.${reviewReminder.status}`} />
                  </td>
                  <td>
                    <Translate contentKey={`adaptiveQuizzApp.ReminderKind.${reviewReminder.kind}`} />
                  </td>
                  <td>
                    {reviewReminder.createdAt ? <TextFormat type="date" value={reviewReminder.createdAt} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {reviewReminder.doneAt ? <TextFormat type="date" value={reviewReminder.doneAt} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{reviewReminder.user ? reviewReminder.user.login : ''}</td>
                  <td>
                    {reviewReminder.question ? (
                      <Link to={`/question/${reviewReminder.question.id}`}>{reviewReminder.question.prompt}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/review-reminder/${reviewReminder.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/review-reminder/${reviewReminder.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/review-reminder/${reviewReminder.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="adaptiveQuizzApp.reviewReminder.home.notFound">No Review Reminders found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={reviewReminderList && reviewReminderList.length > 0 ? '' : 'd-none'}>
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

export default ReviewReminder;
