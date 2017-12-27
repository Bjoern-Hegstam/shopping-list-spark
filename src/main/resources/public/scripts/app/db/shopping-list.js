define(['jquery'], function ($) {
    function ajaxErrorHandler(xhr, status, error) {
        console.log(xhr.responseText);
    }

    return {
        createShoppingList: function (name) {
            return $.ajax({
                url: '/api/shopping-list/',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({
                    name: name
                }),
                error: ajaxErrorHandler
            })
        }
    };
});