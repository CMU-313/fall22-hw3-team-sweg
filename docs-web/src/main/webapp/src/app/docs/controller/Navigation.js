'use strict';

/**
 * Navigation controller.
 */
angular.module('docs').controller('Navigation', function($scope, $state, $stateParams, $rootScope, User, Restangular) {
  User.userInfo().then(function(data) {
    $rootScope.userInfo = data;
    if (data.anonymous) {
      if($state.current.name !== 'login') {
        $state.go('login', {
          redirectState: $state.current.name,
          redirectParams: JSON.stringify($stateParams),
        }, {
          location: 'replace'
        });
      }
    }
  });

  $scope.unreadCount = 0;
  async function getUnreadMessageCount() {
    let response = await Restangular.one("messages", "unread_count").get({count: $scope.unreadCount});
    $scope.unreadCount = response.count;
    await getUnreadMessageCount();
  }

  getUnreadMessageCount();

  /**
   * User logout.
   */
  $scope.logout = function($event) {
    User.logout().then(function() {
      User.userInfo(true).then(function(data) {
        $rootScope.userInfo = data;
      });
      $state.go('main');
    });
    $event.preventDefault();
  };
});