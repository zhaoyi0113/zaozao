define(['angular', 'user', 'login', 'course', 'courseplan', 'coursetag', 'homeconfig', 'user_service', 'login_service'], function(angular) {

    'use strict';
    var home = angular.module("homeModule", ['userModule', 'loginModule', 'courseModule', 'coursePlanModel',
        'courseTagModule', 'homeConfigModule', 'userServiceModule', 'loginServiceModule'
    ]);
    home.controller('HomeController', ['$scope', '$http', '$rootScope', 'LoginService',
        function($scope, $http, $rootScope, loginService) {
            console.log('home');
            var loginLabel = '';
            if (loginService.isLogin() === true) {
                loginLabel = 'Logout';
            } else {
                loginLabel = 'Login';
            }
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
                label: loginLabel
            }];
            console.log(loginService.sha1('jstc127a'));
            $rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams) {
                console.log('state changed to ' + toState.name)
                if (toState.name == 'login') {
                    return; // 如果是进入登录界面则允许
                }
                // 如果用户不存在
                // if (!$rootScope.user || !$rootScope.user.token) {
                //     event.preventDefault(); // 取消默认跳转行为
                //     $state.go("login", {
                //         from: fromState.name,
                //         w: 'notLogin'
                //     }); //跳转到登录界面
                // }
            });
        }
    ]);
});