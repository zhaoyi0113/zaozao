define(['angular', 'jquery', 'angular-file-upload', 'angular-animate'], function(angular, $) {
	var homeConfig = angular.module("homeConfigModule", ['angularFileUpload', 'ngAnimate']);

	homeConfig.controller('HomeConfigController', ['$scope', '$http', '$location', '$state', 'FileUploader',
		function($scope, $http, $location, $state, FileUploader) {
			$scope.uploader = new FileUploader({
				url: $location.protocol() + '://' + $location.host() + ":" + $location.port() +
					'/education/zaozao/homeconfig',
				formData: []
			});
			
			getHomeConfigImages($scope, $http, $location);

			$scope.deletePic = function(id) {
				console.log('remove image ', id);
				$http.delete($location.protocol() + '://' + $location.host() + ":" + $location.port() +
						'/education/zaozao/homeconfig/'+id)
				.success(function(e){
					getHomeConfigImages($scope, $http, $location);
				}).error(function(e){
					console.error('failed to delete ',e);
				});
			}

			$scope.submit = function() {
				console.log('upload all images');
				$scope.uploader.uploadAll();
			}
			$scope.uploader.onAfterAddingFile = function(fileItem) {
				for (var i = 0; i < $scope.uploader.queue.length; i++) {
					if (fileItem !== $scope.uploader.queue[i]) {
						$scope.uploader.queue[i].remove();
					}
				}
				console.info('onAfterAddingFile', fileItem);
			};
			$scope.uploader.onCompleteAll = function() {
                console.info('onCompleteAll');
                getHomeConfigImages($scope, $http, $location);
            };
		}
	]);

	function getHomeConfigImages($scope, $http, $location){
		$http.get($location.protocol() + "://" + $location.host() + ":" + $location.port() +
					'/education/zaozao/homeconfig')
				.success(function(e) {
					console.log('get images ', e);
					$scope.images = new Array();
					for (var i = 0; i < e.length; i++) {
						$scope.images[i] = {
							id: e[i].id,
							url: e[i].image,
							active: false,
							index: i,
							name: e[i].fileName
						};
						console.log('image ', $scope.images[i]);
					}
					if($scope.images.length>0){
						$scope.currentImage = $scope.images[0];
						$scope.currentId = $scope.images[0].id;
						$scope.currentImage.active = true;
					}
				}).error(function(e) {
					console.log('failed to get images ', e);
				});
	}
	
});