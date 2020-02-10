import { combineReducers } from 'redux';
import auth from './UserReducer';
import shoppingList from './ShoppingListReducer';
import loading from './LoadingReducer';
import error from './ErrorReducer';

export default combineReducers({
  auth,
  shoppingList,
  request: combineReducers({
    loading,
    error,
  }),
});
