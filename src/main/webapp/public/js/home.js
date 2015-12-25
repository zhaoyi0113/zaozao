define(['angular','user', 'login','course','courseplan','coursetag'], function(angular){

    'use strict';
    var home = angular.module("homeModule", 
        ['userModule', 'loginModule', 'courseModule', 'coursePlanModel', 'courseTagModule']);
    home.controller('HomeController', ['$scope', '$http', function($scope, $http){
        console.log('home');
        $scope.tabs=[
        {
            url: '.course',
            label: 'Course'
        }
        ,{
            url: '.coursetag',
            label: 'Course Tag'
        }
        ];
    }]);
});