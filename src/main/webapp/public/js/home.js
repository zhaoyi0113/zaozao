define(['angular', 'user', 'login', 'course', 'courseplan', 'coursetag', 'homeconfig', 'user_service', 'login_service'], function(angular) {

    'use strict';
    var home = angular.module("homeModule", ['userModule', 'loginModule', 'courseModule', 'coursePlanModel',
        'courseTagModule', 'homeConfigModule', 'userServiceModule', 'loginServiceModule'
    ]);
    home.controller('HomeController', ['$scope', '$http', '$rootScope', 'LoginService','$state',
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
            loginService.isLogin().then(function(event){
                console.log('already login ', event);
                $scope.tabs[3].label = 'Logout';
            }, function(error){
                console.log('not login', error);
                $scope.tabs[3].label = 'Login';
            });
            $rootScope.$on('LOGIN', function(event, data){
                console.log('login service changed ',data);

                if(data === true){
                    $scope.tabs[3].label = 'Logout';

                }else{
                    $scope.tabs[3].label = 'Login';
                }
            });
        }
    ]);
});