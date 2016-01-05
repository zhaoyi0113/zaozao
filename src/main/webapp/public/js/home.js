define(['angular', 'user', 'login', 'course', 'courseplan',
    'coursetag', 'homeconfig', 'user_service', 'login_service', 'course_video',
], function(angular) {

    'use strict';
    var home = angular.module("homeModule", ['userModule', 'loginModule', 'courseModule', 'coursePlanModel',
        'courseTagModule', 'homeConfigModule', 'userServiceModule', 
        'loginServiceModule', 'courseVideoModule'
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
                url: '.course_video',
                label: 'Course Video'
            },{
                url: '.config',
                label: 'Home Page Slide'
            }, {
                url: '.login',
                label: 'Label'
            }];
            $scope.adminTabs = [{
                url: '.user',
                label: 'User'
            }];
            $scope.showAdmin = false;
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
                $scope.showAdmin = false;
            });

            function loginSuccess(event) {
                $scope.tabs[3].label = 'Logout';
                if (event === 'admin') {
                    console.log('login as admin');
                    $scope.showAdmin = true;
                } else if (event === 'user') {
                    console.log('login as user');
                    $scope.showAdmin = false;
                };
            }
        }
    ]);

});