define(['angular', 'angular-bootstrap'], function(angular) {

	'use strict';
	var course = angular.module("courseAnalyticsModule", ['ui.bootstrap']);

	course.controller('CourseAnalyticsController', ['$scope', '$location', '$state', '$http',
		function($scope, $location, $state, $http) {
			$scope.number = 20;
			$scope.pageIdx = 1;
			$scope.headers = ['Name', 'Publish Date', 'PV(Page View)'];
			loadCourseAnalytics();
			$http.get($location.protocol() + "://" + $location.host() + ":" +
					$location.port() + '/education/zaozao/course/coursecount')
				.success(function(e) {
					$scope.totalCourseCount = e;
					$scope.totalPages = Math.round(e / $scope.number + 0.5);
					console.log('total pages ', $scope.totalPages);
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

	course.controller('CourseAnalyticsDetailController', ['$scope', '$http',
		function($scope, $http) {

		}
	]);
});