define(['angular','user', 'login','course','courseplan', 'kindeditor','kindeditor-zh'], function(angular){

    'use strict';
    var home = angular.module("homeModule", 
        ['userModule', 'loginModule', 'courseModule', 'coursePlanModel']);
    home.controller('HomeController', ['$scope', '$http', function($scope, $http){
        console.log('home');
        $scope.tabs=[
        {
            url: '.course',
            label: 'Course'
        },{
            url: '.courseplan',
            label: 'Course Plan'
        }
        ];
    }]);
});