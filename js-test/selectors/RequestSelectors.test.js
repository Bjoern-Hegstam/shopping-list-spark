import { createErrorSelector } from '../../js/selectors/RequestSelectors';

const ACTION_TYPE = 'ACTION';

describe('LoadingSelector', () => {

});

describe('ErrorSelector', () => {
  it('when error exists', () => {
    const error = {
      status: 400,
    };
    const state = {
      request: {
        error: {
          [ACTION_TYPE]: error,
        },
      },
    };

    expect(createErrorSelector(ACTION_TYPE)(state)).toEqual(error);
  });
});
