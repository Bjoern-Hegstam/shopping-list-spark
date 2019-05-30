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
    [ACTION_TYPE]: null,
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
  const state = { [ACTION_TYPE]: null };

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

describe('request with meta requestId', () => {
  const requestId1 = 'requestId1';
  const requestId2 = 'requestId2';

  it('clears error when new request initiated -- error state is empty', () => {
    // given
    const action = {
      type: ACTION_TYPE,
      meta: { requestId: requestId1 },
      payload: { request: {} },
    };

    // when
    const newState = reducer({}, action);

    // then
    expect(newState).toEqual({
      [ACTION_TYPE]: {
        [requestId1]: null,
      },
    });
  });

  it('clears error when new request initiated -- error state contains previous requests', () => {
    // given
    const action = {
      type: ACTION_TYPE,
      meta: { requestId: requestId1 },
      payload: { request: {} },
    };
    const oldState = {
      [ACTION_TYPE]: {
        [requestId1]: 'ERROR_CODE',
        [requestId2]: 'ERROR_CODE',
      },
    };

    // when
    const newState = reducer(oldState, action);

    // then
    expect(newState).toEqual({
      [ACTION_TYPE]: {
        [requestId1]: null,
        [requestId2]: 'ERROR_CODE',
      },
    });
  });

  it('ignores SUCCESS', () => {
    // given
    const action = {
      type: `${ACTION_TYPE}_SUCCESS`,
      meta: { requestId: requestId1 },
      payload: { request: {} },
    };

    // when
    const newState = reducer({}, action);

    // then
    expect(newState).toEqual({});
  });

  it('stores error message on FAIL', () => {
    // given
    const state = {
      [ACTION_TYPE]: {
        [requestId1]: null,
        [requestId2]: null,
      },
    };

    const action = {
      type: `${ACTION_TYPE}_FAIL`,
      meta: { requestId: requestId1 },
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
        [requestId1]: {
          status: 400,
          data: {
            errorCode: 'ERROR_CODE',
          },
        },
        [requestId2]: null,
      },
    });
  });
});
