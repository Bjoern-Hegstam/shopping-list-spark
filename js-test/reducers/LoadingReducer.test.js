import reducer from '../../js/reducers/LoadingReducer';

const ACTION_TYPE = 'GET_DATA';

it('ignores action without request payload', () => {
  // given
  const action = { type: ACTION_TYPE };

  // when
  const newState = reducer({}, action);

  // then
  expect(newState).toEqual({});
});

it('marks request as loading', () => {
  // given
  const action = {
    type: ACTION_TYPE,
    payload: { request: {} },
  };

  // when
  const newState = reducer({}, action);

  // then
  expect(newState).toEqual({
    [ACTION_TYPE]: true,
  });
});

it('marks request as not loading on SUCCESS', () => {
  // given
  const state = { [ACTION_TYPE]: true };

  const action = {
    type: `${ACTION_TYPE}_SUCCESS`,
    payload: { request: {} },
  };

  // when
  const newState = reducer(state, action);

  // then
  expect(newState).toEqual({
    [ACTION_TYPE]: false,
  });
});

it('marks request as not loading on FAIL', () => {
  // given
  const state = { [ACTION_TYPE]: true };

  const action = {
    type: `${ACTION_TYPE}_FAIL`,
    payload: { request: {} },
  };

  // when
  const newState = reducer(state, action);

  // then
  expect(newState).toEqual({
    [ACTION_TYPE]: false,
  });
});
