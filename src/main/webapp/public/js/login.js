define(['angular'], function (angular) {
    'use strict';
    var home = angular.module("loginModule", []);
    home.controller('LoginController', ['$scope', '$http', '$state', '$httpParamSerializer', '$location',
        function ($scope, $http, $state, $httpParamSerializer, $location) {
            $scope.username = '';
            $scope.password = '';
            $scope.loginFailed = false;

            $http.get('http://' + $location.host() + ":" + $location.port() + '/education/zaozao/login/check')
                .success(function(e) {
                    $scope.login=true;
                }).error(function(e){
                    $scope.login=false;
                });

            $scope.submit = function (e) {
                var req = {
                    method: 'POST',
                    url: 'http://' + $location.host() + ":" + $location.port() + '/education/zaozao/login',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
                    },
                    data: $httpParamSerializer({
                        userName: $scope.username,
                        password: $scope.password
                    })

                };
                $http(req).success(function (e) {
                    $state.go('home');
                }).error(function (e) {
                    $scope.loginFailed = true;
                });
            };

            $scope.logout = function(e){
                $http.get('http://' + $location.host() + ":" + $location.port() + '/education/zaozao/login/logout')
                    .success(function(e){
                        $state.go('home');
                    });
            }
        }]);


});