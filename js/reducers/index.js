import {combineReducers} from 'redux';
import user from './UserReducer';
import shoppingList from './ShoppingListReducer';

export default combineReducers({
    user,
    shoppingList
});
