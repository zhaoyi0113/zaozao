define(['angular', 'user_service', 'ui-router', 'jquery', 'login_service'], function(angular) {
	'use strict';

	var user = angular.module("userPrivilegeModule", []);

	user.controller('UserPrivilegeController', ['$scope', '$http', '$location', '$state', '$httpParamSerializer',
		function($scope, $http, $location, $state, $httpParamSerializer) {

			$http.get($location.protocol()+'://'+$location.host()+':'+$location.port()+'/education/zaozao/user_profile')
			.success(function(e){
				console.log(e);
				$scope.userName = !!e.userName;
				$scope.userImage = !!e.userImage;
				$scope.childName = !!e.childName;
				$scope.childBirthdate = !!e.childBirthdate;
				$scope.childGender = !!e.childGender;
			}).error(function(e){

			});

			$scope.submit = function() {
				$http({
                    method: 'POST',
                    url: $location.protocol()+'://' + $location.host() + ":" + $location.port() + 
                        '/education/zaozao/user_profile',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
                    },
                    data: $httpParamSerializer({
                        user_name: $scope.userName?1:0,
                        user_image: $scope.userImage?1:0,
                        child_name: $scope.childName?1:0,
                        child_birthdate: $scope.childBirthdate?1:0,
                        child_gender: $scope.childGender?1:0
                    })
                }).success(function(e){
                	$state.go('home');
                }).error(function(e){

                });
			}


		}
	]);
});