define(['angular','angular-file-upload','directives','angular-ui-date','angular-bootstrap','angular-ui-bootstrap-datetimepicker'], function(angular){
    'use strict';
    var course = angular.module("courseModule", ['angularFileUpload', 'ngThumbModel','ui.bootstrap', 'ui.bootstrap.datetimepicker']);
    course.controller('CourseController', ['$scope', '$http','$location','$state',
        function($scope, $http,$location, $state){
        $http.get('http://'+$location.host()+":"+$location.port()+'/education/zaozao/course/queryall')
        .success(function(e){
            var str = JSON.stringify(e);
            var json = JSON.parse(str);
            console.log('get all course ', json);
            $scope.courses = json;
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
                url: 'http://'+$location.host()+":"+$location.port()+'/education/zaozao/course/uploadfile',
                formData: []
            });
            $scope.dateOptions = {
                startingDay: 1,
                showWeeks: true
            };
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
            $scope.uploader.onCompleteAll = function() {
                console.info('onCompleteAll');
                $state.go('home.course');
            };
            $scope.uploader.onBeforeUploadItem = function(item) {
                console.info('onBeforeUploadItem', item);
                item.formData.push({name: $scope.name});
                item.formData.push({content: $scope.content});
                item.formData.push({category: $scope.category});
                item.formData.push({date: $scope.date});
            };

            $scope.submit = function(){
                console.log('create new course ', $scope.date);
                //$scope.uploader.uploadAll();
            }
        }]);

    course.controller('CourseEditController', ['$scope', '$http','$stateParams','$state','$location',
        'FileUploader',
        function($scope, $http, $stateParams, $state, $location,FileUploader){

            console.log('edit course id=', $stateParams.courseId);

            $http.get('http://'+$location.host()+":"+$location.port()+'/education/zaozao/course/querycourse/'+$stateParams.courseId)
                .success(function(e){
                    var json = JSON.parse(JSON.stringify(e));
                    $scope.course = json;
                    console.log('course:', $scope.course);
                    $scope.course.imageurl = 'http://'+$location.host()+":"+$location.port()+
                        '/education/public/resources/courses/'+$scope.course.id+'/'+$scope.course.picture_paths;

                }).error(function(e){
                    console.log('error:',e);
                    var ret = JSON.stringify(e);
                    var json = JSON.parse(ret);
                    if('-10' === json.status){
                        $state.go('home.login');
                    }
                });
            $scope.uploader = new FileUploader({
                url: 'http://'+$location.host()+":"+$location.port()+'/education/zaozao/course/edit',
                formData: []
            });

            $scope.submit = function(){

            }
            $scope.cancel = function(){
                $state.go('home.course');
            }

            $scope.uploader.onCompleteAll = function() {
                console.info('onCompleteAll');
                $state.go('home.course');
            };
            $scope.uploader.onBeforeUploadItem = function(item) {
                console.info('onBeforeUploadItem', item);
                item.formData.push({id: $scope.course.id});
                item.formData.push({name: $scope.course.name});
                item.formData.push({content: $scope.course.content});
                item.formData.push({category: $scope.course.category});
                item.formData.push({date: $scope.course.date});
            };
    }]);
});