define(['angular', 'login_service'], function(angular) {
    'use strict';
    var home = angular.module("loginModule", ['loginServiceModule']);
    home.controller('LoginController', ['$scope', '$http', '$state',
        '$httpParamSerializer', '$location', 'LoginService','$stateParams',
        function($scope, $http, $state, $httpParamSerializer, $location, loginSrv,
            $stateParams) {
            
            console.log('state params, '+$stateParams.state);
            $scope.username = '';
            $scope.password = '';
            $scope.loginFailed = false;
            loginSrv.isLogin().then(function(e) {
                console.log('logout ');
                loginSrv.doLogout();
            }, function(error) {
            });

            $scope.submit = function(e) {
                loginSrv.doLogin($scope.username, $scope.password)
                    .then(function(event) {
                        if($stateParams.state !== null 
                            && $stateParams.state !== undefined){
                            $state.go($stateParams.state);
                        }else{
                            $state.go('home');
                        }
                    }, function(error) {
                        $scope.loginFailed = true;
                    });
            };

            $scope.logout = function(e) {
                loginSrv.doLogout()
                .then(function(e) {
                        $state.go('home');
                    });
            };

        }
    ]);


});