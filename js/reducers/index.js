import { combineReducers } from 'redux';
import auth from './UserReducer';
import entities from './EntitiesReducer';
import loading from './LoadingReducer';
import error from './ErrorReducer';

export default combineReducers({
    auth,
    entities,
    request: combineReducers({
        loading,
        error,
    }),
});
