define(['jquery', 'app/db/shopping-list', 'app/db/item-type', 'selectize'], function ($, listDb, itemTypeDb) {
    // Wire up shopping list buttons
    var $shoppingList = $('.shopping-list');
    var $addItemModal = $('#addItemModal');
    var $nameInput = $addItemModal.find('#nameInput');

    var $nameInputSelect = $nameInput
        .selectize({
            valueField: 'id',
            labelField: 'name',
            searchField: 'name',
            load: function(query, callback) {
                if (!query.length) {
                    return callback();
                }

                itemTypeDb
                    .findItemTypesWithNameLike(query, 5)
                    .done(function (result) {
                        callback(result);
                    });
            },
            create: function(input, callback) {
                var self = this;
                itemTypeDb
                    .createItemType(input)
                    .done(function(result) {
                        $addItemModal.modal('hide');
                        addToShoppingList(result.id);

                        // Must be called or the selectized input can't be used
                        // again without a reload.
                        callback();
                    });
            },
            onItemAdd: function(value, $item) {
                $addItemModal.modal('hide');
                addToShoppingList(value);
            }
        });


    $addItemModal
        .on('shown.bs.modal', function focusOnNameInput() {
            $nameInputSelect[0].selectize.focus();
        });


    $addItemModal
        .on('hidden.bs.modal', function clearNameInput() {
            $nameInputSelect[0].selectize.clear();
        });


    // Shopping list item event handlers are attached to the wrapping ul-tag to
    // keep triggering the handlers for dynamically added items.
    var $shoppingListItems = $('.shopping-list-items');

    $shoppingListItems
        .on('click', '.shopping-list-item', function toggleItemInCart(e) {
            var $shoppingListItem = $(this);
            var isInCart = $shoppingListItem.hasClass('in-cart');

            listDb
                .updateShoppingListItem(
                    getId($shoppingList),
                    getId($shoppingListItem),
                    {
                        in_cart: !isInCart
                    }
                )
                .done(function(result) {
                    $shoppingListItem.toggleClass('in-cart');
                });
        });


    $shoppingListItems
        .on('click', '.btn-item-inc', function incrementQuantity(e) {
            e.stopPropagation();

            updateItemQuantity($(this).parents('.shopping-list-item'), 1);
        });

    $shoppingListItems
        .on('click', '.btn-item-dec', function decrementQuantity(e) {
            e.stopPropagation();

            updateItemQuantity($(this).parents('.shopping-list-item'), -1);
        });


    function getId($object) {
        return $object.attr('data-id');
    }


    function addToShoppingList(itemTypeId) {
        listDb
            .addToShoppingList(getId($shoppingList), itemTypeId)
            .done(function(result) {
                // TODO: Format and append shopping list item html
                $('.shopping-list-items').append(result);
            });
    }


    function updateItemQuantity($shoppingListItem, dQuantity) {
        var $quantity = $shoppingListItem.find('.quantity');
        var quantity = +($quantity.text());

        if (quantity + dQuantity <= 0) {
            listDb
                .deleteShoppingListItem(getId($shoppingList),  getId($shoppingListItem))
                .done(function() {
                    $shoppingListItem.remove();
                });
            return;
        }

        listDb
            .updateShoppingListItem(
                getId($shoppingList),
                getId($shoppingListItem),
                {
                    quantity: quantity + dQuantity
                }
            )
            .done(function (result) {
                $quantity.html(result.shopping_list_item.quantity);
            });
    }


    $shoppingList
        .find('.btn-finish-shopping')
        .click(function clearInCartItems() {
            var $inCartItems = $shoppingList.find('.in-cart');
            if ($inCartItems.length === 0) {
                return;
            }

            listDb
                .deleteItemsInCart(getId($shoppingList))
                .done(function(result) {
                    $shoppingList
                        .find('.in-cart')
                        .remove();
                });
        });
});