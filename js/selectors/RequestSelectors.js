export const createLoadingSelector = (...actionTypes) => state => actionTypes.some(type => state.request.loading[type]);

export const createSubRequestLoadingSelector = actionType => state => state.request.loading[actionType] || {};

export const createErrorSelector = (...actionTypes) => state => {
  if (actionTypes.length === 1) {
    return state.request.error[actionTypes[0]] || null;
  }

  return actionTypes.map(type => state.request.error[type]).filter(e => !!e);
};
