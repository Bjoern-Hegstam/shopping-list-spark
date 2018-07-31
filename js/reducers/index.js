import {combineReducers} from 'redux';
import auth from './UserReducer';
import shoppingList from './ShoppingListReducer';
import itemType from './ItemTypeReducer';

export default combineReducers({
    auth,
    shoppingList,
    itemType
});
