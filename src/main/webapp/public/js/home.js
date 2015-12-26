define(['angular','user', 'login','course','courseplan','coursetag','homeconfig'], function(angular){

    'use strict';
    var home = angular.module("homeModule", 
        ['userModule', 'loginModule', 'courseModule', 'coursePlanModel', 'courseTagModule', 'homeConfigModule']);
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
        },{
            url: '.config',
            label: 'Home Page Config'
        }
        ];
    }]);
});