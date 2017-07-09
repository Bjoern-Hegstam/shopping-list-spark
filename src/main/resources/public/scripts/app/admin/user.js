define(['jquery'], function ($) {

    $('.user-role-selector').on('change', function setUserRole() {
        var $userControl = $(this);
        var userId = getUserId($userControl);
        var newRole = $userControl.val();

        console.log('User ' + userId + ": " + newRole);
    });

    $('.user-verified-toggle').on('change', function setVerifiedState() {
        var $userControl = $(this);
        var userId = getUserId($userControl);
        var newVerifiedState = $userControl.is(':checked');

        console.log('User ' + userId + ': ' + newVerifiedState);
    });

    function getUserId($userControl) {
        return $userControl.closest('.user-list-item').attr('data-user-id');
    }
});
