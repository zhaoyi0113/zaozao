define(['angular','user_service','ui-router'], function(angular) {
    'use strict';

    var user = angular.module("userModule", ['userService','ui.router']);
    user.controller('UserController', ['$scope', '$http', '$location','UserService', function($scope, $http, $location, userSrv){
        console.log('user');
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

    user.controller('UserEditController', ['$scope', '$http', '$stateParams','UserService','$state','$location',
        '$httpParamSerializer',
            function($scope, $http, $stateParams, userSrv, $state, $location, $httpParamSerializer){
        console.log("params", $stateParams.userId);
        $scope.userId = $stateParams.userId;
        $scope.user = userSrv.getUserById($scope.userId);
        console.log("user", $scope.user);

        $scope.submit = function(){

            var req = {
                method: 'POST',
                url: 'http://'+$location.host()+":"+$location.port()+'/education/zaozao/editor/user',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
                },
                data: $httpParamSerializer({ user_id: $scope.user.userId,
                    user_name: $scope.user.userName,
                    phone: $scope.user.phone,
                    age: $scope.user.age,
                    email: $scope.user.email,
                    birthdate: $scope.user.birthdate})

            };
            $http(req).success(function(e){
                $state.go('home.user');
                userSrv.updateUser($scope.user);
            }).error(function(e){

            });
        }
        $scope.cancel = function(){
            $state.go('home.user');
        }
    }]);


});