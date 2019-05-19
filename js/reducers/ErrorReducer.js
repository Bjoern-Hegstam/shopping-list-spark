export default function (state = {}, action) {
  const { type } = action;

  let requestName;
  let error;

  const matches = /(.*)_FAIL/.exec(type);
  if (matches) {
    [, requestName] = matches;
    const { status, data } = action.error.response;
    error = {
      status,
      data,
    };
  } else if (action.payload && action.payload.request && !type.endsWith('SUCCESS')) {
    requestName = action.type;
    error = '';
  } else {
    return state;
  }

  return {
    ...state,
    [requestName]: error,
  };
}
