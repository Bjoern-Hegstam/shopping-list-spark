export const hasId = id => item => item.id === id;
export const hasRequestId = requestId => item => item.requestId === requestId;
export const not = predicate => params => !predicate(params);
