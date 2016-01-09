define(['angular', 'angular-file-upload', 'admin_pwd_service', 'login_service', 'angular-bootstrap', 'ng-dialog'], function(angular) {
	'use strict';
	var course = angular.module("courseVideoModule", ['angularFileUpload', 'adminPwdServiceModule',
		'loginServiceModule', 'ui.bootstrap', 'ngDialog'
	]);
	course.controller('CourseVideoController', ['$scope', '$http', '$location',
		'$state', 'AdminPwdService', 'LoginService', '$stateParams', 'ngDialog',
		function($scope, $http, $location, $state, adminPwdSrv, loginSrv, $stateParams, ngDialog) {
			$scope.headers = ['Name', 'Publish Date', 'Status', 'Video'];
			$scope.pageIdx = 1;
			$scope.number = 10;
			loadCourses();
			$http.get('http://' + $location.host() + ":" +
					$location.port() + '/education/zaozao/course/coursecount')
			.success(function(e){
				$scope.totalCourseCount = e;
				$scope.totalPages = Math.round(e/$scope.number+0.5);
				console.log('total pages ', $scope.totalPages);
			});
			$scope.pageChanged=function(){
				console.log('page change to '+$scope.pageIdx);
				loadCourses();
			}

			function loadCourses(){
				$http.get('http://' + $location.host() + ":" +
					$location.port() + '/education/zaozao/course/queryall?page_index=' + ($scope.pageIdx-1) +
					'&number=' + $scope.number)
				.success(function(e) {
					var str = JSON.stringify(e);
					var json = JSON.parse(str);
					console.log('get all course ', json);
					$scope.courses = json;
				}).error(function(e) {

				});
			}
		}
	]);

	course.controller('CourseVideoEditController', ['$scope',
		'$http', '$stateParams', '$location', 'FileUploader', '$state', '$sce',
		function($scope, $http, $stateParams, $location, FileUploader, $state, $sce) {
			$scope.courseId = $stateParams.courseId;
			if ($scope.courseId === '' || $scope.courseId === undefined) {
				$state.go('home.course_video');
			}
			$scope.uploader = new FileUploader({
				url: 'http://' + $location.host() + ":" +
					$location.port() + '/education/zaozao/course/video',
				formData: []
			});
			$scope.uploadProgress = 0;
			getVideo($scope, $http, $location);
			$scope.submit = function() {
				$scope.uploader.uploadAll();
			}
			$scope.cancel = function() {
				$state.go('home.course_video');
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

			$scope.uploader.onProgressAll = function(progress) {
				console.info('onProgressAll', progress);
				$scope.uploadProgress = progress;
			};

			function getVideo($scope, $http, $location) {
				$http.get('http://' + $location.host() + ":" +
						$location.port() + '/education/zaozao/course/video/' + $scope.courseId)
					.success(function(e) {
						console.log('course ', e);
						$scope.courseName = e.course_name;
						$scope.videoPath = $sce.trustAsResourceUrl(e.video_path);
						console.log('get video path ', $scope.videoPath);
					}).error(function(e) {

					});
			}
		}
	]);

});