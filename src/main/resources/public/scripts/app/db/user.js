define(['jquery'], function ($) {
    function ajaxErrorHandler(xhr, status, error) {
        console.log(xhr.responseText);
    }

    function updateUser(userId, data) {
        return $.ajax({
            url: '/api/user/' + userId,
            type: 'PATCH',
            contentType: 'application/json',
            data: JSON.stringify(data),
            error: ajaxErrorHandler
        });
    }

    return {
        setRole: function (userId, role) {
            return updateUser(userId, {
                role: role
            });
        },

        setVerified: function (userId, isVerified) {
            return updateUser(userId, {
                verified: isVerified
            });
        }
    };
});