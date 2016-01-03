define(['angular', 'user_service', 'ui-router','jquery','login_service'], function(angular) {
    'use strict';

    var user = angular.module("userModule", ['userServiceModule', 'ui.router','loginServiceModule']);

    user.controller('UserController', ['$scope', '$http', '$location', 'UserService', '$state',
        function($scope, $http, $location, userSrv, $state) {
            console.log('user');
            $scope.headers = ['Name', 'Role', 'Edit', 'Delete'];
            $scope.users = {};
            getUsers($scope, $http, $location);
            $scope.delete = function(id){
                $http.delete('http://' + $location.host() + ":" + $location.port() + '/education/zaozao/backend_users/'+id)
                .success(function(e){
                    console.log('delete user '+id+' success');
                    getUsers($scope, $http, $location);
                }).error(function(e){
                    console.log('failed to delete user ', e);
                });
            }
        }
    ]);

    function getUsers($scope, $http, $location){
        $http.get('http://' + $location.host() + ":" + $location.port() + '/education/zaozao/backend_users')
                .success(function(e) {
                    console.log('get response ', e);
                    if (e !== 'Not Login') {
                        $scope.users = e;
                    } else {
                        
                    }
                }).error(function(e) {

                });
    }

    user.controller('UserEditController', ['$scope', '$http', '$stateParams', 'UserService', '$state', '$location',
        '$httpParamSerializer', 'LoginService',
        function($scope, $http, $stateParams, userSrv, $state, $location, $httpParamSerializer, loginSrv) {
            console.log("params", $stateParams.userId);
            $scope.userId = $stateParams.userId;
            $http.get($location.protocol()+"://"+$location.host()+":"+$location.port()+
                "/education/zaozao/backend_users/"+$scope.userId)
                .success(function(e){
                    console.log('get user ',e);
                    $scope.user = e;
                }).error(function(e){

                });

            $scope.submit = function() {

                var req = {
                    method: 'POST',
                    url: $location.protocol()+'://' + $location.host() + ":" + $location.port() + 
                        '/education/zaozao/backend_users/edit',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
                    },
                    data: $httpParamSerializer({
                        id: $scope.user.id,
                        name: $scope.user.name,
                        password: loginSrv.sha1($scope.user.password)
                    })
                };
                $http(req).success(function(e) {
                    $state.go('home.user');
                }).error(function(e) {

                });
            }
            $scope.cancel = function() {
                $state.go('home.user');
            }
        }
    ]);

    user.controller('NewUserController', ['$scope', '$http', '$httpParamSerializer', '$location', '$state','LoginService',
        function($scope, $http, $httpParamSerializer, $location, $state, loginSrv) {
            console.log("add user");
            $scope.user = {};

            $http.get('http://' + $location.host() + ":" + 
                $location.port() + '/education/zaozao/backend_users/get_roles')
            .success(function(e){
                $scope.roles = e;
                console.log('get roles:',e);
                for(var i =0; i<e.length; i++){
                    if(e[i].roleName === 'user'){
                        $scope.userRoleId=e[i].id;
                        break;
                    }
                }
            }).error(function(e){

            });
            $scope.submit = function() {
                var req = {
                    method: 'POST',
                    url: 'http://' + $location.host() + ":" + $location.port() + '/education/zaozao/backend_users',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
                    },
                    data: $httpParamSerializer({
                        name: $scope.user.userName,
                        password: loginSrv.sha1($scope.user.password),
                        role_id: $scope.userRoleId
                    })
                };
                $http(req).success(function(e) {
                    $state.go('home.user');
                }).error(function(e) {

                });
            }
            $scope.cancel = function() {
                $state.go('home.user');
            }
        }
    ]);

    user.directive('pwCheck', [function () {
      return {
          require: 'ngModel',
          link: function (scope, elem, attrs, ctrl) {
              var firstPassword = '#' + attrs.pwCheck;
              console.log('pwcheck=', attrs.pwCheck);
              elem.add(firstPassword).on('keyup', function () {
                  scope.$apply(function () {
                      var v = elem.val()===$(firstPassword).val();
                      console.log('password:',v);
                     ctrl.$setValidity('pwmatch', v);
                 });
             });
         }
     }
    }]);

});