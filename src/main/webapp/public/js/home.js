define(['angular','user', 'login'], function(angular){

    'use strict';
    var home = angular.module("homeModule", ['userModule', 'loginModule']);
    home.controller('HomeController', ['$scope', '$http', function($scope, $http){
        console.log('home');
        $scope.tabs=[{
            url: '.user',
            label: 'User'
        },{
            url: '.adduser',
            label: 'New User'
        },{
            url: '.login',
            label: 'Login/Logout'
        }];
    }]);
});