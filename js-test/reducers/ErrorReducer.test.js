import reducer from '../../js/reducers/ErrorReducer';

const ACTION_TYPE = 'GET_DATA';

it('ignores non-fail action without request payload', () => {
  // given
  const action = { type: ACTION_TYPE };

  // when
  const newState = reducer({}, action);

  // then
  expect(newState).toEqual({});
});

it('clears error when new request initiated', () => {
  // given
  const action = {
    type: ACTION_TYPE,
    payload: { request: {} },
  };

  // when
  const newState = reducer({}, action);

  // then
  expect(newState).toEqual({
    [ACTION_TYPE]: '',
  });
});

it('ignores SUCCESS', () => {
  // given
  const action = {
    type: `${ACTION_TYPE}_SUCCESS`,
    payload: { request: {} },
  };

  // when
  const newState = reducer({}, action);

  // then
  expect(newState).toEqual({});
});

it('stores error message on FAIL', () => {
  // given
  const state = { [ACTION_TYPE]: '' };

  const action = {
    type: `${ACTION_TYPE}_FAIL`,
    error: {
      response: {
        status: 400,
        data: {
          errorCode: 'ERROR_CODE',
        },
      },
    },
  };

  // when
  const newState = reducer(state, action);

  // then
  expect(newState).toEqual({
    [ACTION_TYPE]: {
      status: 400,
      data: {
        errorCode: 'ERROR_CODE',
      },
    },
  });
});
