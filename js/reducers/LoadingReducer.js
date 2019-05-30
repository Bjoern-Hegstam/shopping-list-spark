export default function (state = {}, action) {
  const { type } = action;

  let actionType;
  let isLoading;

  const matches = /(.*)_(?:SUCCESS|FAIL)/.exec(type);
  if (matches) {
    [, actionType] = matches;
    isLoading = false;
  } else if (action.payload?.request) {
    actionType = type;
    isLoading = true;
  } else {
    return state;
  }

  if (action.meta?.requestId) {
    return {
      ...state,
      [actionType]: {
        ...state[actionType],
        [action.meta.requestId]: isLoading,
      },
    };
  }

  return {
    ...state,
    [actionType]: isLoading,
  };
}
