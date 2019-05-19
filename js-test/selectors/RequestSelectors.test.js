import { createErrorSelector, createLoadingSelector } from '../../js/selectors/RequestSelectors';

const ACTION_TYPE_1 = 'ACTION_1';
const ACTION_TYPE_2 = 'ACTION_2';
const ACTION_TYPE_3 = 'ACTION_3';
const ACTION_TYPE_4 = 'ACTION_4';
const UNKNOWN_ACTION_TYPE = 'UNKNOWN';

describe('LoadingSelector', () => {
  const state = {
    request: {
      loading: {
        [ACTION_TYPE_1]: true,
        [ACTION_TYPE_2]: false,
        [ACTION_TYPE_3]: true,
        [ACTION_TYPE_4]: false,
      },
    },
  };

  it('when there is no entry for given action type', () => {
    expect(createLoadingSelector(UNKNOWN_ACTION_TYPE)(state)).toBe(false);
  });

  it('when ongoing request for action type exists', () => {
    expect(createLoadingSelector(ACTION_TYPE_1)(state)).toBe(true);
  });

  it('when ongoing request for action type does not exist', () => {
    expect(createLoadingSelector(ACTION_TYPE_2)(state)).toBe(false);
  });

  it('when multiple action types given and some have ongoing requests', () => {
    expect(createLoadingSelector(ACTION_TYPE_1, ACTION_TYPE_2, ACTION_TYPE_3)(state))
      .toBe(true);
  });

  it('when multiple action types given and none ongoing requests', () => {
    expect(createLoadingSelector(ACTION_TYPE_2, ACTION_TYPE_4)(state))
      .toBe(false);
  });
});

describe('ErrorSelector', () => {
  const action1Error = {
    status: 401,
  };

  const action3Error = {
    status: 404,
  };

  const state = {
    request: {
      error: {
        [ACTION_TYPE_1]: action1Error,
        [ACTION_TYPE_2]: null,
        [ACTION_TYPE_3]: action3Error,
        [ACTION_TYPE_4]: null,
      },
    },
  };

  it('when there is no entry for given action type', () => {
    expect(createErrorSelector(UNKNOWN_ACTION_TYPE)(state)).toBeNull();
  });

  it('when there is no error', () => {
    expect(createErrorSelector(ACTION_TYPE_2)(state)).toBeNull();
  });

  it('when error exists', () => {
    expect(createErrorSelector(ACTION_TYPE_1)(state)).toEqual(action1Error);
  });

  it('when multiple action types given and some have errors', () => {
    expect(createErrorSelector(ACTION_TYPE_1, ACTION_TYPE_2, ACTION_TYPE_3)(state))
      .toEqual([action1Error, action3Error]);
  });

  it('when multiple action types given and none have errors', () => {
    expect(createErrorSelector(ACTION_TYPE_2, ACTION_TYPE_4)(state))
      .toHaveLength(0);
  });
});
