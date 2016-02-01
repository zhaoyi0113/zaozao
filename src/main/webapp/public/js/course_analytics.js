define(['angular', 'angular-bootstrap'], function(angular) {

	'use strict';
	var course = angular.module("courseAnalyticsModule", ['ui.bootstrap']);

	course.controller('CourseAnalyticsController', ['$scope', '$location', '$state', '$http',
		function($scope, $location, $state, $http) {
			$scope.number = 10;
			$scope.pageIdx = 1;
			$scope.headers = ['Name', 'Publish Date', 'PV(Page View)'];
			loadCourseAnalytics();
			$http.get($location.protocol() + "://" + $location.host() + ":" +
					$location.port() + '/education/zaozao/course/coursecount')
				.success(function(e) {
					$scope.totalCourseCount = e;
					$scope.totalPages = Math.round(e / $scope.number + 0.5);
					console.log('total pages ', $scope.totalPages);
					console.log('total items ', $scope.totalCourseCount);

				});
			$http.get($location.protocol() + "://" + $location.host() + ":" +
					$location.port() + '/education/zaozao/course/total_course_pv')
				.success(function(e) {
					$scope.totalPV = e;


				});
			$scope.pageChanged = function() {
				loadCourseAnalytics();
			}

			function loadCourseAnalytics() {
				$http.get($location.protocol() + "://" + $location.host() + ":" + $location.port() +
						'/education/zaozao/course/queryall?page_index=' + ($scope.pageIdx - 1) + '&number=' + $scope.number)
					.success(function(e) {
						$scope.courses = e;
						for (var i = 0; i < $scope.courses.length; i++) {
							$scope.courses[i].editable = false;
							$scope.courses[i].button = 'Edit';
						}
					}).error(function(e) {

					});
			}

			$scope.edit = function(course) {
				course.editable = !course.editable;
				if (course.editable) {
					course.button = "Confirm";
				} else {
					course.button = "Edit";
				}

			}
		}
	]);

	course.controller('CourseAnalyticsDetailController', ['$scope', '$http', '$stateParams', '$location',
		function($scope, $http, $stateParams, $location) {
			$scope.courseId = $stateParams.courseId;
			$scope.pageIdx = 1;
			$scope.number = 10;
			loadCourseAnalytics();
			$scope.headers = ['User Name', 'Date', 'Access Type'];
			$scope.userAccessList = [{
				flag: 'aaa'
			}];
			$http.get($location.protocol() + "://" + $location.host() + ":" + $location.port() +
					'/education/zaozao/course/querycourse/' + $scope.courseId)
				.success(function(e) {
					console.log('get course:', e);
					$scope.course = e;
				}).error(function(e) {

				});
			$http.get($location.protocol() + "://" + $location.host() + ":" + $location.port() +
					'/education/zaozao/course/analytics_count?id=' + $scope.courseId)
				.success(function(e) {
					console.log('get course count:', e);
					$scope.totalCourseCount = e;
					$scope.totalPages = Math.round(e / $scope.number + 0.5);
					console.log('total page:', $scope.totalPages);
				}).error(function(e) {

				});

			function loadCourseAnalytics() {
				$http.get($location.protocol() + "://" + $location.host() + ":" + $location.port() +
						'/education/zaozao/course/analytics?id=' + $scope.courseId + '&page_index=' + ($scope.pageIdx - 1) + '&number=' + $scope.number)
					.success(function(e) {
						console.log('get course analytics:', e);
						$scope.userAccessList = e;
						for (var i = 0; i < e.length; i++) {
							$scope.userAccessList[i].accessDate =
								new Date(e[i].accessDate);
							var date = new Date();
							var utc = $scope.userAccessList[i].accessDate.getTime() + date.getTimezoneOffset() * 60000;
							var calcTime = utc + 3600000 * 8;
							$scope.userAccessList[i].accessDate = (new Date(calcTime)).toLocaleString();

						}
					}).error(function(e) {

					});
			}

			$scope.pageChanged = function() {
				console.log('page changed to ' + $scope.pageIdx);
				loadCourseAnalytics();
			}
		}
	]);
});