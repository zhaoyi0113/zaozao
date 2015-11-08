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

            //$scope.uploader.bind('beforeupload', function (event, item) {
            //
            //    var index = uploader.getIndexOfItem(item);
            //
            //    item.formData.push({title: index});
            //});

            //$scope.uploader.formData.push({id:'aaa'});

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
                item.formData.push({id: 'aaa'});
                item.formData.push({name: $scope.name});
                item.formData.push({content: $scope.content});
                item.formData.push({category: $scope.category});
                item.formData.push({date: $scope.date});
            };

            $scope.submit = function(){

                $scope.uploader.uploadAll();

                //var req = {
                //    method: 'POST',
                //    url: 'http://'+$location.host()+":"+$location.port()+'/education/zaozao/course/new',
                //    headers: {
                //        'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
                //    },
                //    data: $httpParamSerializer({
                //        name: $scope.name,
                //        content: $scope.content,
                //        category: $scope.category,
                //        date: $scope.date
                //    })
                //};
                //$http(req).success(function(e){
                //
                //    $scope.uploader.formData.push({id: 'aaa'});
                //    $scope.uploader.uploadAll();
                //    $state.go('home.course');
                //
                //}).error(function(e){
                //
                //});

            }
        }]);
});