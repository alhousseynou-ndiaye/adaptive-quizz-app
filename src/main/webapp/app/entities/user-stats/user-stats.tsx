import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { TextFormat, Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './user-stats.reducer';

export const UserStats = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const userStatsList = useAppSelector(state => state.userStats.entities);
  const loading = useAppSelector(state => state.userStats.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="user-stats-heading" data-cy="UserStatsHeading">
        <Translate contentKey="adaptiveQuizzApp.userStats.home.title">User Stats</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="adaptiveQuizzApp.userStats.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/user-stats/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="adaptiveQuizzApp.userStats.home.createLabel">Create new User Stats</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {userStatsList && userStatsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="adaptiveQuizzApp.userStats.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('currentDifficulty')}>
                  <Translate contentKey="adaptiveQuizzApp.userStats.currentDifficulty">Current Difficulty</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('currentDifficulty')} />
                </th>
                <th className="hand" onClick={sort('totalAnswered')}>
                  <Translate contentKey="adaptiveQuizzApp.userStats.totalAnswered">Total Answered</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('totalAnswered')} />
                </th>
                <th className="hand" onClick={sort('totalCorrect')}>
                  <Translate contentKey="adaptiveQuizzApp.userStats.totalCorrect">Total Correct</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('totalCorrect')} />
                </th>
                <th className="hand" onClick={sort('streakDays')}>
                  <Translate contentKey="adaptiveQuizzApp.userStats.streakDays">Streak Days</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('streakDays')} />
                </th>
                <th className="hand" onClick={sort('lastActiveAt')}>
                  <Translate contentKey="adaptiveQuizzApp.userStats.lastActiveAt">Last Active At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastActiveAt')} />
                </th>
                <th>
                  <Translate contentKey="adaptiveQuizzApp.userStats.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="adaptiveQuizzApp.userStats.subject">Subject</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {userStatsList.map((userStats, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/user-stats/${userStats.id}`} color="link" size="sm">
                      {userStats.id}
                    </Button>
                  </td>
                  <td>{userStats.currentDifficulty}</td>
                  <td>{userStats.totalAnswered}</td>
                  <td>{userStats.totalCorrect}</td>
                  <td>{userStats.streakDays}</td>
                  <td>
                    {userStats.lastActiveAt ? <TextFormat type="date" value={userStats.lastActiveAt} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{userStats.user ? userStats.user.login : ''}</td>
                  <td>{userStats.subject ? <Link to={`/subject/${userStats.subject.id}`}>{userStats.subject.name}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/user-stats/${userStats.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/user-stats/${userStats.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/user-stats/${userStats.id}/delete`)}
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
              <Translate contentKey="adaptiveQuizzApp.userStats.home.notFound">No User Stats found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default UserStats;
