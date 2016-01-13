define(['angular', 'angular-file-upload', 'admin_pwd_service', 'login_service'], function(angular) {
	'use strict';
	var course = angular.module("courseTagModule", ['angularFileUpload', 'adminPwdServiceModule', 'loginServiceModule']);

	course.controller('CourseTagController', ['$scope', '$http', '$location', '$state', 'AdminPwdService', 'LoginService',
		function($scope, $http, $location, $state, adminPwdSrv, loginSrv) {
			$scope.headers = ['Name', 'Delete'];
			$http.get($location.protocol() + '://' + $location.host() + ":" + $location.port() + '/education/zaozao/course_tags')
				.success(function(e) {
					console.log('get all course ', e);
					$scope.courseTags = e;
				}).error(function(e) {

				});
			$scope.newCourseTag = function() {
				$state.go('home.coursetag_new');
			}

			$scope.delete = function(id) {
				console.log('delete course tag ' + id);
				loginSrv.isLogin().then(function(event) {
					console.log(event === 'admin');
					if (event !== 'admin') {
						adminPwdSrv.openPasswordDlg().then(function(data) {
							if (data.value !== true) {
								return;
							}
							$scope.adminPwd = adminPwdSrv.adminPwd;
							deleteCourseTag(id);
						});
					} else {
						deleteCourseTag(id);
					}
				});
			}

			function deleteCourseTag(id) {
				$http.delete($location.protocol() + '://' + $location.host() + ":" + $location.port() +
						'/education/zaozao/course_tags/' + id, {
							headers: {
								'password': $scope.adminPwd
							}
						})
					.success(function(e) {
						var tag = null;
						var i = 0;
						for (i = 0; i < $scope.courseTags.length; i++) {
							if ($scope.courseTags[i].id === id) {
								tag = $scope.courseTags[i];
								break;
							}
						}
						if (tag !== null) {
							$scope.courseTags.splice(i, 1);
						}
					}).error(function(e) {
						console.log('delete course tag error ', e);
					});
			}

		}
	]);

	course.controller('NewCourseTagController', ['$scope', '$http', '$location', '$state', 'FileUploader',
		'AdminPwdService', 'LoginService',
		function($scope, $http, $location, $state, FileUploader, adminPwdSrv, loginSrv) {
			$scope.uploader = new FileUploader({
				url: $location.protocol() + '://' + $location.host() + ":" + $location.port() + '/education/zaozao/course_tags',
				formData: []
			});
			$scope.submit = function() {

				loginSrv.isLogin().then(function(event) {
					console.log(event === 'admin');
					if (event !== 'admin') {
						adminPwdSrv.openPasswordDlg().then(function(data) {
							console.log('close dialog ', data.value, adminPwdSrv.adminPwd);
							$scope.adminPwd = adminPwdSrv.adminPwd;
							if (data.value !== true) {
								return;
							}
							$scope.uploader.uploadAll();
						});
					} else {
						$scope.uploader.uploadAll();
					}

				}, function(error) {

				});

			}
			$scope.cancel = function() {
				console.log('cancel');
				$state.go('home.coursetag');
			}
			$scope.uploader.onAfterAddingFile = function(fileItem) {
				for (var i = 0; i < $scope.uploader.queue.length; i++) {
					if (fileItem !== $scope.uploader.queue[i]) {
						$scope.uploader.queue[i].remove();
					}
				}
				console.info('onAfterAddingFile', fileItem);
			};
			$scope.uploader.onBeforeUploadItem = function(item) {
				console.info('onBeforeUploadItem', item);
				item.formData.push({
					tag_name: $scope.name,
					status: $scope.courseStatus
				});
				item.headers = {
					password: $scope.adminPwd
				};

			};
			$scope.uploader.onCompleteAll = function() {
				console.info('onCompleteAll');
				$state.go('home.coursetag');
			};
			$scope.uploader.onErrorItem = function(item, response, status, headers) {
				alert('Add course tag failed');
			};

		}
	]);
	course.controller('EditCourseTagController', ['$scope', '$http', '$location', '$state', 'FileUploader',
		'$httpParamSerializer', '$stateParams', 'AdminPwdService', 'LoginService', 
		function($scope, $http, $location, $state, FileUploader, $httpParamSerializer, $stateParams,
			adminPwdSrv, loginSrv) {

			console.log('edit course id = ', $stateParams.courseTagId);
			$scope.uploader = new FileUploader({
				url: $location.protocol() + '://' + $location.host() + ":" + $location.port() + '/education/zaozao/course_tags',
				formData: []
			});

			$http.get($location.protocol() + '://' + $location.host() + ":" + $location.port() + '/education/zaozao/course_tags/' + $stateParams.courseTagId)
				.success(function(e) {
					console.log('get course tag ', e);
					$scope.name = e.name;
					$scope.imageUrl = e.imageUrl;
				});

			$scope.submit = function() {
				console.log('submit');
				loginSrv.isLogin().then(function(event) {
					console.log(event === 'admin');
					if (event !== 'admin') {
						adminPwdSrv.openPasswordDlg().then(function(data) {
							console.log('close dialog ', data.value, adminPwdSrv.adminPwd);
							if (data.value !== true) {
								return;
							}
							$scope.adminPwd = adminPwdSrv.adminPwd;
							
							editCourseTag();
						});
					} else {
						editCourseTag();
					}
				}, function(error) {

				});

			}
			$scope.cancel = function() {
				$state.go('home.coursetag');
			}
			$scope.uploader.onAfterAddingFile = function(fileItem) {
				for (var i = 0; i < $scope.uploader.queue.length; i++) {
					if (fileItem !== $scope.uploader.queue[i]) {
						$scope.uploader.queue[i].remove();
					}
				}
				console.info('onAfterAddingFile', fileItem);
			};
			$scope.uploader.onBeforeUploadItem = function(item) {
				console.info('onBeforeUploadItem', item);
				item.formData.push({
					tag_name: $scope.name,
					course_tag_id: $stateParams.courseTagId

				});
				item.headers={password: $scope.adminPwd};
			};
			$scope.uploader.onCompleteAll = function() {
				console.info('onCompleteAll');
				$state.go('home.coursetag');
			};

			
			$scope.uploader.onErrorItem = function(item, response, status, headers) {
				alert('Add course tag failed');
			};

			function editCourseTag() {
				if ($scope.uploader.queue.length > 0) {
					$scope.uploader.uploadAll();
				} else {
					//upload without image
					var req = {
						method: 'POST',
						url: $location.protocol() + '://' + $location.host() + ":" + $location.port() + '/education/zaozao/course_tags/edit',
						headers: {
							'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',
							'password': $scope.adminPwd
						},
						data: $httpParamSerializer({
							course_tag_id: $stateParams.courseTagId,
							tag_name: $scope.name,
							status: $scope.courseStatus
						})
					};
					$http(req).success(function(e) {
						console.log('edit course tag success.');
						$state.go('home.coursetag');
					}).error(function(e) {
						console.log('edic course tag error ', e);
						alert('failed to edit course tag');
					});
				}
			}
		}
	]);

});