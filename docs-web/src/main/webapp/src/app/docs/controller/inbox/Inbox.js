'use strict';

/**
 * Inbox controller.
 */
angular.module('docs').controller('Inbox', function($scope, Restangular) {

    // get list of user's messages from server
    $scope.loadMessages = function() {
        Restangular.one('messages').get().then(function(data) {
            $scope.messages = data.messages;
        });
    };

    $scope.loadMessages();

    $scope.readMessage = function(msgId) {
        Restangular.one('messages', 'read').post('', {id: msgId});
    };
});