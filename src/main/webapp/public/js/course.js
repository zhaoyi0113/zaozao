define(['angular','angular-file-upload','directives'], function(angular){
    'use strict';
    var course = angular.module("courseModule", ['angularFileUpload', 'ngThumbModel']);
    course.controller('CourseController', ['$scope', '$http','$location','$state',
        function($scope, $http,$location, $state){
        $http.get('http://'+$location.host()+":"+$location.port()+'/education/zaozao/course/queryall')
        .success(function(e){
            var str = JSON.stringify(e);
            var json = JSON.parse(str);
            console.log('get all course ', json);
        }).error(function(e){

        });
        $scope.newCourse = function(){
            $state.go('home.newcourse')
        }
    }]);

    course.controller('NewCourseController', ['$scope', '$http', '$location', '$state','FileUploader',
        '$httpParamSerializer',
        function($scope, $http, $location, $state, FileUploader, $httpParamSerializer){
            $scope.uploader = new FileUploader({
                url: ''
            });
            $http.get('http://'+$location.host()+":"+$location.port()+'/education/zaozao/coursetype')
                .success(function(e){
                    var str = JSON.stringify(e);
                    var json = JSON.parse(str);
                    $scope.categories = new Array();
                    for(var i=0; i<json.length;i++){
                        $scope.categories[i] = json[i].name;
                    }
                    if($scope.categories.lenght>0){
                        $scope.defaultCategory = $scope.categories[0];
                    }
                    console.log('get course type ', $scope.categories);

                }).error(function(e){

                });
            $scope.submit = function(){
                var req = {
                    method: 'POST',
                    url: 'http://'+$location.host()+":"+$location.port()+'/education/zaozao/course/new',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
                    },
                    data: $httpParamSerializer({
                        name: $scope.name,
                        content: $scope.content,
                        category: $scope.category,
                        date: $scope.date
                    })
                };
                $http(req).success(function(e){
                    $state.go('home.course');

                }).error(function(e){

                });
            }
        }]);
});