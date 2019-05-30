export default function (state = {}, action) {
  const { type } = action;

  let actionType;
  let error;

  const failActionMatch = /(.*)_FAIL/.exec(type);
  if (failActionMatch) {
    [, actionType] = failActionMatch;
    const { status, data } = action.error.response;
    error = {
      status,
      data,
    };
  } else if (action.payload?.request && !type.endsWith('SUCCESS')) {
    actionType = action.type;
    error = null;
  } else {
    return state;
  }

  if (action.meta?.requestId) {
    return {
      ...state,
      [actionType]: {
        ...state[actionType],
        [action.meta.requestId]: error,
      },
    };
  }

  return {
    ...state,
    [actionType]: error,
  };
}
