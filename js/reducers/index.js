import { combineReducers } from 'redux';
import auth from './UserReducer';
import shoppingList from './ShoppingListReducer';
import itemType from './ItemTypeReducer';
import loading from './LoadingReducer';
import error from './ErrorReducer';

export default combineReducers({
    auth,
    shoppingList,
    itemType,
    request: combineReducers({
        loading,
        error,
    }),
});
