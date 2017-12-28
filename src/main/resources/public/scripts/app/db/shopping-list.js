define(['jquery'], function ($) {
    function ajaxErrorHandler(xhr, status, error) {
        console.log(xhr.responseText);
    }

    return {
        createShoppingList: function (name) {
            var data = {
                name: name
            };

            return $.ajax({
                url: '/api/shopping_list',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(data),
                error: ajaxErrorHandler
            });
        },

        addToShoppingList: function(listId, itemTypeId) {
            var data = {
                shopping_list_item: {
                    item_type_id: itemTypeId,
                    quantity: 1
                }
            };

            return $.ajax({
                url: '/api/shopping_list/' + listId + '/item',
                type: 'POST',
                contentType: 'application/json',
                accept: 'text/html',
                data: JSON.stringify(data),
                error: ajaxErrorHandler
            });
        },

        deleteShoppingListItem: function(listId, listItemId) {
            return $.ajax({
                url: '/api/shopping_list/' + listId + '/item/' + listItemId,
                type: 'DELETE',
                error: ajaxErrorHandler
            });
        },

        updateShoppingListItem: function(listId, listItemId, values) {
            var data = {
                shopping_list_item: values
            };

            return $.ajax({
                url: '/api/shopping_list/' + listId + '/item/' + listItemId,
                type: 'PATCH',
                contentType: 'application/json',
                data: JSON.stringify(data),
                error: ajaxErrorHandler
            });
        },

        deleteItemsInCart: function(listId) {
            return $.ajax({
                url: '/api/shopping_list/' + listId + '/cart',
                type: 'DELETE',
                error: ajaxErrorHandler
            });
        }
    };
});