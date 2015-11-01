define(['angular','user_service','ui-router'], function(angular) {
    'use strict';

    var user = angular.module("userModule", ['userService','ui.router']);

    user.controller('UserController', ['$scope', '$http', '$location','UserService', function($scope, $http, $location, userSrv){
        $scope.headers=['Name', 'Email', 'Phone','Edit'];
        $http.get('http://'+$location.host()+":"+$location.port()+'/education/zaozao/query/allusers').success(function(e){
            console.log('get response');
            var json = JSON.parse(JSON.stringify(e));
            console.log("json:", json);
            $scope.users = json;
            userSrv.setUserList(json);
        }).error(function(e){

        });
    }]);

    user.controller('UserEditController', ['$scope', '$http', '$stateParams','UserService','$state',
            function($scope, $http, $stateParams, userSrv, $state){
        console.log("params", $stateParams.userId);
        $scope.userId = $stateParams.userId;
        $scope.user = userSrv.getUserById($scope.userId);
        console.log("user", $scope.user);

        $scope.submit = function(){

        }
        $scope.cancel = function(){
            $state.go('home.user');
        }
    }]);


});