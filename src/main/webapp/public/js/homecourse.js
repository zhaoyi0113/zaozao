define(['angular', 'jquery',
	'bootstrap-select', 'angular-bootstrap-select'
], function(angular, $) {
	var homeCourse = angular.module("homeCourseModule", ['angular-bootstrap-select', 'angular-bootstrap-select.extra']);
	homeCourse.controller('HomeCourseController', 
		['$scope', '$http', '$location','$httpParamSerializer',
		 function($scope, $http, $location, $httpParamSerializer) {
		getHomeCourses($scope, $http, $location);
		$scope.selectedCourseId = '';
		$http.get($location.protocol() + "://" + $location.host() +
				":" + $location.port() + '/education/zaozao/course/query/allnames')
			.success(function(e) {
				// console.log('get course idnames ',e);
				$scope.courseIdNames = new Array();
				for (var c in e) {
					$scope.courseIdNames.push({
						id: c,
						name: e[c]
					});
				}
				console.log('get course idnames:', $scope.courseIdNames[0]);

			});
		$scope.submit = function() {
			console.log('selectedCourseId=', $scope.selectedCourseId.id);
			$http({
				method: 'POST',
				url: $location.protocol()+'://' + $location.host() + ":" + $location.port() +
					'/education/zaozao/homecourse',
				headers: {
					'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
				},
				data: $httpParamSerializer({
					course_id: $scope.selectedCourseId.id
				})
			}).success(function(e) {
				getHomeCourses($scope, $http, $location);
			}).error(function(data, status, headers, config) {
				console.log('status:', status);
				if(status == 10040){
					alert('Duplicate Course');
				}
			});
		}

		$scope.moveUp = function(course){
			move(course, 'UP');
		}

		$scope.moveDown = function(course){
			move(course, 'DOWN');
		}

		function move(course, action){
			$http({
				method: 'POST',
				url: $location.protocol()+'://' + $location.host() + ":" + $location.port() +
					'/education/zaozao/homecourse/move',
				headers: {
					'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
				},
				data: $httpParamSerializer({
					id: course.id,
					action: action
				})
			}).success(function(e) {
				getHomeCourses($scope, $http, $location);
			}).error(function(e) {

			});
		}
		$scope.deleteCourse = function(id){
			$http.delete($location.protocol()+'://' + $location.host() + ":" + $location.port() +
					'/education/zaozao/homecourse/'+id)
			.success(function(e){
				getHomeCourses($scope, $http, $location);
			});
		}
	}]);

	function getHomeCourses($scope, $http, $location) {
		$http.get($location.protocol() + "://" + $location.host() + ":" + $location.port() +
				'/education/zaozao/homecourse')
			.success(function(e) {
				console.log('coures ', e);
				$scope.courses = e;
			}).error(function(e) {

			});
	}

});