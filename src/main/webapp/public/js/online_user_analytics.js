define(['angular', 'angular-bootstrap'], function(angular) {

	'use strict';
	var course = angular.module("onlineUserAnalyticsModule", ['ui.bootstrap']);

	course.controller('OnlineUserAnalyticsController', ['$scope',
		'$location', '$state', '$http',
		function($scope, $location, $state, $http) {
			$scope.pageIdx = 1;
			$scope.number = 20;
			$scope.headers = ['Name', 'Register Date'];
			loadUsers();

			$http.get($location.protocol() + "://" + $location.host() + ":" +
						$location.port() + '/education/zaozao/online/user_count')
					.success(function(e) {
						console.log('all users count ',e);
						$scope.totalCourseCount = e;
						$scope.totalPages = Math.round(e / $scope.number + 0.5);
					}).error(function(e) {

					});

			function loadUsers() {
				$http.get($location.protocol() + "://" + $location.host() + ":" +
						$location.port() + '/education/zaozao/online/allusers?page_index='+
						($scope.pageIdx-1)+"&number="+$scope.number)
					.success(function(e) {
						console.log('all users ',e);
						$scope.users = e;
					}).error(function(e) {

					});
			}
		}
	]);
});