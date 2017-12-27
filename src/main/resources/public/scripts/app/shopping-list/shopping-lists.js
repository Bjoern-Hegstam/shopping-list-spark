define(['jquery', 'app/db/shopping-list'], function ($, db) {
    var $createShoppingListModal = $('#createShoppingListModal');
    var $listNameInput = $createShoppingListModal.find('#nameInput');

    $createShoppingListModal
        .on('shown.bs.modal', function focusOnNameInput() {
            $listNameInput.focus();
        });


    $createShoppingListModal
        .on('hidden.bs.modal', function clearNameInput() {
            $listNameInput.clear();
        });

    $listNameInput.keypress(function(e) {
        if (e.which === 13) {
            e.stopPropagation();

            db.createShoppingList($listNameInput.val());
            $createShoppingListModal.modal('hide');
        }
    });
});