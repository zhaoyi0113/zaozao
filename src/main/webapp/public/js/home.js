define(['angular', 'user', 'login', 'course', 'courseplan', 'coursetag', 'homeconfig', 'user_service', 'login_service'], function(angular) {

    'use strict';
    var home = angular.module("homeModule", ['userModule', 'loginModule', 'courseModule', 'coursePlanModel',
        'courseTagModule', 'homeConfigModule', 'userServiceModule', 'loginServiceModule'
    ]);
    home.controller('HomeController', ['$scope', '$http', '$rootScope', 'LoginService', '$state',
        function($scope, $http, $rootScope, loginService, $state) {
            console.log('home');
            $scope.tabs = [{
                url: '.course',
                label: 'Course'
            }, {
                url: '.coursetag',
                label: 'Course Tag'
            }, {
                url: '.config',
                label: 'Home Page Config'
            }, {
                url: '.login',
                label: 'Label'
            }];
            loginService.isLogin().then(function(event) {
                loginSuccess(event);
            }, function(error) {
                console.log('not login', error);
                $scope.tabs[3].label = 'Login';
            });
            $rootScope.$on('LOGIN', function(event, data) {
                console.log('login service changed ', data);

                if (data === false) {
                    $scope.tabs[3].label = 'Login';

                } else {
                    loginSuccess(data);
                }
            });
            $rootScope.$on('LOGOUT', function(event, data) {
                if ($scope.tabs.length === 5) {
                    $scope.tabs.splice(4, 1);
                }
            });

            function loginSuccess(event) {
                console.log('already login ', event);
                $scope.tabs[3].label = 'Logout';
                if (event === 'admin' && $scope.tabs.length === 4) {
                    $scope.tabs.push({
                        url: '.user',
                        label: 'User'
                    });
                } else if (event === 'user' && $scope.tabs.length === 5) {
                    $scope.tabs.splice(4, 1);
                };
            }
        }
    ]);

});