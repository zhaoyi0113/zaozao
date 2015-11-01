define(['angular','user'], function(angular){

    'use strict';
    var home = angular.module("homeModule", ['userModule']);
    home.controller('HomeController', ['$scope', '$http', function($scope, $http){
        $scope.tabs=[{
            url: '.user',
            label: 'User'
        }];
    }]);
});