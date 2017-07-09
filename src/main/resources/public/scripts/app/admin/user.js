define(['jquery', 'app/db/user'], function ($, db) {

    $('.user-role-selector').on('change', function setUserRole() {
        var $userControl = $(this);
        var userId = getUserId($userControl);
        var newRole = $userControl.val();

        db.setRole(userId, newRole);
    });

    $('.user-verified-toggle').on('change', function setVerifiedState() {
        var $userControl = $(this);
        var userId = getUserId($userControl);
        var newVerifiedState = $userControl.is(':checked');

        db.setVerified(userId, newVerifiedState);
    });

    function getUserId($userControl) {
        return $userControl.closest('.user-list-item').attr('data-user-id');
    }
});
