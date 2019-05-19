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
  } else if (action.payload && action.payload.request && !type.endsWith('SUCCESS')) {
    actionType = action.type;
    error = '';
  } else {
    return state;
  }

  return {
    ...state,
    [actionType]: error,
  };
}
