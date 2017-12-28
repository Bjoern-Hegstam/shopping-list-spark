define(['jquery', 'app/db/item-type'], function ($, db) {
    $('.item-type')
        .on('click', '.btn-item-type-delete', function deleteItemType(e) {
            var itemType = $(this).closest('.item-type');
            var itemTypeId = itemType.attr('data-id');
            db.deleteItemType(itemTypeId)
                .done(function() {
                    itemType.remove();
                });
        });
});
