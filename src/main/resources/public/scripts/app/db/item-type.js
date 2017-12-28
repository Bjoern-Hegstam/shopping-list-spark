define(['jquery'], function ($) {
    function ajaxErrorHandler(xhr, status, error) {
        console.log(xhr.responseText);
    }

    return {
        createItemType: function(name) {
            var data = {
                name: name
            };

            return $.ajax({
                url: '/api/item-type/',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(data),
                error: ajaxErrorHandler
            });
        },

        findItemTypesWithNameLike: function (nameStart, limit) {
            return $.ajax({
                url: '/api/item-type?name=' + nameStart + '&limit=' + limit,
                type: 'GET',
                error: ajaxErrorHandler
            });
        }
    };
});