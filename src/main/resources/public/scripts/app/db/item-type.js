define(['jquery'], function ($) {
    function ajaxErrorHandler(xhr, status, error) {
        console.log(xhr.responseText);
    }

    return {
        createItemType: function(name) {
            var data = {
                item_type: {
                    name: name
                }
            };

            return $.ajax({
                url: '/api/item_type',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(data),
                error: ajaxErrorHandler
            });
        },

        findItemTypesWithNameLike: function (nameStart, limit) {
            return $.ajax({
                url: '/api/item_type?name=' + nameStart + '&limit=' + limit,
                type: 'GET',
                error: ajaxErrorHandler
            });
        }
    };
});