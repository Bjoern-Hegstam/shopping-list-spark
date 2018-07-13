import {combineReducers} from 'redux';
import auth from './UserReducer';
import shoppingList from './ShoppingListReducer';

export default combineReducers({
    auth,
    shoppingList
});
