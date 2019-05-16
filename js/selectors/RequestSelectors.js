export const createLoadingSelector = (...actionTypes) => state => actionTypes
  .some(type => state.request.loading[type]);

export const createErrorSelector = (...actionTypes) => state => actionTypes
  .find(type => state.request.error[type]) || null;
