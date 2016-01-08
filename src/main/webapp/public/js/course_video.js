define(['angular', 'angular-file-upload', 'admin_pwd_service', 'login_service'], function(angular) {
	'use strict';
	var course = angular.module("courseVideoModule", ['angularFileUpload', 'adminPwdServiceModule', 'loginServiceModule']);
	course.controller('CourseVideoController', ['$scope', '$http', '$location',
		'$state', 'AdminPwdService', 'LoginService', '$stateParams',
		function($scope, $http, $location, $state, adminPwdSrv, loginSrv, $stateParams) {
			$scope.headers = ['Name', 'Publish Date', 'Status', 'Video'];

			$http.get('http://' + $location.host() + ":" +
					$location.port() + '/education/zaozao/course/queryall')
				.success(function(e) {
					var str = JSON.stringify(e);
					var json = JSON.parse(str);
					console.log('get all course ', json);
					$scope.courses = json;
				}).error(function(e) {

				});
		}
	]);

	course.controller('CourseVideoEditController', ['$scope',
		'$http', '$stateParams', '$location', 'FileUploader', '$state',
		function($scope, $http, $stateParams, $location, FileUploader, $state) {
			$scope.courseId = $stateParams.courseId;
			if ($scope.courseId === '' || $scope.courseId === undefined) {
				$state.go('home.course_video');
			}
			$scope.uploader = new FileUploader({
				url: 'http://' + $location.host() + ":" +
					$location.port() + '/education/zaozao/course/video',
				formData: []
			});
			getVideo($scope, $http, $location);
			$scope.submit = function() {
				$scope.uploader.uploadAll();
			}

			$scope.uploader.onBeforeUploadItem = function(item) {
				console.info('onBeforeUploadItem', item);
				item.formData.push({
					course_id: $scope.courseId
				});
			};

			$scope.uploader.onCompleteAll = function() {
				console.info('onCompleteAll');
				getVideo($scope, $http, $location);

			};

			function getVideo($scope, $http, $location) {
				$http.get('http://' + $location.host() + ":" +
						$location.port() + '/education/zaozao/course/video/' + $scope.courseId)
					.success(function(e) {
						console.log('course ', e);
						$scope.videoPath = $sce.trustAsResourceUrl(e.video_path);
						console.log('get video path ', $scope.videoPath);
					}).error(function(e) {

					});
			}
		}
	]);

});