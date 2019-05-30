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

describe('request with meta requestId', () => {
  const requestId1 = 'requestId1';
  const requestId2 = 'requestId2';

  it('marks request as loading -- loading state is empty', () => {
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
        [requestId1]: true,
      },
    });
  });

  it('marks request as loading -- loading state contains previous requests', () => {
    // given
    const action = {
      type: ACTION_TYPE,
      meta: { requestId: requestId1 },
      payload: { request: {} },
    };
    const oldState = {
      [ACTION_TYPE]: {
        [requestId2]: false,
      },
    };

    // when
    const newState = reducer(oldState, action);

    // then
    expect(newState).toEqual({
      [ACTION_TYPE]: {
        [requestId1]: true,
        [requestId2]: false,
      },
    });
  });


  it('marks request as not loading on SUCCESS', () => {
    // given
    const state = {
      [ACTION_TYPE]: {
        [requestId1]: true,
        [requestId2]: true,
      },
    };

    const action = {
      type: `${ACTION_TYPE}_SUCCESS`,
      meta: { requestId: requestId1 },
      payload: { request: {} },
    };

    // when
    const newState = reducer(state, action);

    // then
    expect(newState).toEqual({
      [ACTION_TYPE]: {
        [requestId1]: false,
        [requestId2]: true,
      },
    });
  });

  it('marks request as not loading on FAIL', () => {
    // given
    const state = {
      [ACTION_TYPE]: {
        [requestId1]: true,
        [requestId2]: true,
      },
    };

    const action = {
      type: `${ACTION_TYPE}_FAIL`,
      meta: { requestId: requestId1 },
      payload: { request: {} },
    };

    // when
    const newState = reducer(state, action);

    // then
    expect(newState).toEqual({
      [ACTION_TYPE]: {
        [requestId1]: false,
        [requestId2]: true,
      },
    });
  });
});
