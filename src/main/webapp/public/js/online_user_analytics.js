define(['angular', 'angular-bootstrap'], function(angular) {

	'use strict';
	var course = angular.module("onlineUserAnalyticsModule", ['ui.bootstrap']);

	course.controller('OnlineUserAnalyticsController', ['$scope',
		'$location', '$state', '$http',
		function($scope, $location, $state, $http) {
			$scope.pageIdx = 1;
			$scope.number = 10;
			$scope.headers = ['Name', 'Last Login Time'];
			loadUsers();

			$http.get($location.protocol() + "://" + $location.host() + ":" +
					$location.port() + '/education/zaozao/online/user_count')
				.success(function(e) {
					console.log('all users count ', e);
					$scope.totalCourseCount = e;
					$scope.totalPages = Math.round(e / $scope.number + 0.5);
				}).error(function(e) {

				});

			$scope.pageChanged = function() {
				loadUsers();
			}

			function loadUsers() {
				$http.get($location.protocol() + "://" + $location.host() + ":" +
						$location.port() + '/education/zaozao/online/allusers?page_index=' +
						($scope.pageIdx - 1) + "&number=" + $scope.number)
					.success(function(e) {
						console.log('all users ', e);
						$scope.users = e;
						for (var i = 0; i < e.length; i++) {
							if($scope.users[i].lastLoginTime === null){
								continue;
							}
							var date = new Date();
							var utc = $scope.users[i].lastLoginTime + date.getTimezoneOffset() * 60000;
							var calcTime = utc + 3600000 * 8;
							$scope.users[i].lastLoginTime = (new Date(calcTime)).toLocaleString();
						
						}
					}).error(function(e) {

					});
			}
		}
	]);


	course.controller('OnlineUserDetailAnalyticsController', ['$scope',
		'$location', '$state', '$http', '$stateParams',
		function($scope, $location, $state, $http, $stateParams) {
			console.log('user id ' + $stateParams.userId);
			$scope.userName = $stateParams.userName;
			$scope.pageIdx = 1;
			$scope.number = 10;
			$scope.headers = ['Course Name', 'Date', 'Access'];
			loadUserAccessHistory();

			$http.get($location.protocol() + "://" + $location.host() + ":" +
					$location.port() + "/education/zaozao/online/user_access_count" +
					'?user_id=' + $stateParams.userId)
				.success(function(e) {
					console.log('get user history ');
					$scope.totalCourseCount = e;
					$scope.totalPages = Math.round(e / $scope.number + 0.5);
				}).error(function(e) {

				});

			$scope.pageChanged = function() {
				loadUserAccessHistory();
			}

			function loadUserAccessHistory() {
				$http.get($location.protocol() + "://" + $location.host() + ":" +
						$location.port() + "/education/zaozao/online/user_access_history" +
						'?user_id=' + $stateParams.userId +
						'&page_index=' + ($scope.pageIdx - 1) + "&number=" + $scope.number)
					.success(function(e) {
						console.log('get user history ');

						$scope.users = e;
						for (var i = 0; i < e.length; i++) {
							$scope.users[i].timeCreated = new Date(e[i].timeCreated);
							var date = new Date();
							var utc = $scope.users[i].timeCreated.getTime() + date.getTimezoneOffset() * 60000;
							var calcTime = utc + 3600000 * 8;
							$scope.users[i].timeCreated = (new Date(calcTime)).toLocaleString();
						}
					}).error(function(e) {

					});
			}
		}
	]);
});