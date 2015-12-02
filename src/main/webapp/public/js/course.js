define(['angular', 'angular-file-upload', 'directives', 'angular-ui-date','angular-bootstrap','angular-bootstrap-tpls'
       ,'ueditor-config','ueditor-all', 'angular-editor'], function (angular) {
    'use strict';
    var course = angular.module("courseModule",
        ['angularFileUpload', 'ngThumbModel', 'ui.bootstrap', 'ng.ueditor']);

    course.controller('CourseController', ['$scope', '$http', '$location', '$state',
        function ($scope, $http, $location, $state) {
            console.log("window.location:"+window.location.protocol);
            //window.UEDITOR_HOME_URL = 'http://' + $location.host() + ":" + $location.port() + '/education/zaozao/course/upload_resource';
            $scope.headers = ['Name', 'Category','Date','Delete'];
            $http.get('http://' + $location.host() + ":" + $location.port() + '/education/zaozao/course/queryall')
                .success(function (e) {
                    var str = JSON.stringify(e);
                    var json = JSON.parse(str);
                    console.log('get all course ', json);
                    $scope.courses = json;
                }).error(function (e) {

                });
            $scope.newCourse = function () {
                $state.go('home.newcourse')
            }

            $scope.delete = function(id){
                $http.delete('http://' + $location.host() + ":" + $location.port() + '/education/zaozao/course/'+id)
                    .success(function (e) {
                        console.log('delete success');
                        var i=0;
                        var course = null;
                        for(i=0;i<$scope.courses.length;i++){
                            if(id === $scope.courses[i].id){
                                course = $scope.courses[i];
                                break;
                            }
                        }
                        if(course !== null){
                            $scope.courses.splice(i, 1);
                        }

                    }).error(function(e){
                        console.log(e);
                    });
            };
        }]);

    course.controller('NewCourseController', ['$scope', '$http', '$location', '$state', 'FileUploader',
        '$httpParamSerializer',
        function ($scope, $http, $location, $state, FileUploader, $httpParamSerializer) {
            // $scope.uploader = new FileUploader({
            //     url: 'http://' + $location.host() + ":" + $location.port() + '/education/zaozao/course/uploadfile',
            //     formData: []
            // });
            $scope.status = {};
            $scope.status.opened=false;
            $scope._simpleConfig = {
                 //这里可以选择自己需要的工具按钮名称,此处仅选择如下五个
                 toolbars: [
                   ['FullScreen', 'Source', 'Undo', 'Redo', 'Bold', 'simpleupload']
                 ],
                 //focus时自动清空初始化时的内容
                 autoClearinitialContent: true,
                 //关闭字数统计
                 wordCount: false,
                 //关闭elementPath
                 elementPathEnabled: false
               };
            $scope.open = function($event) {
                $scope.status.opened = true;
            };
            $scope.dateOptions = {
                startingDay: 1,
                showWeeks: true
            };
            $http.get('http://' + $location.host() + ":" + $location.port() + '/education/zaozao/coursetype')
                .success(function (e) {
                    var str = JSON.stringify(e);
                    var json = JSON.parse(str);
                    $scope.categories = new Array();
                    for (var i = 0; i < json.length; i++) {
                        $scope.categories[i] = json[i].name;
                    }
                    if ($scope.categories.lenght > 0) {
                        $scope.defaultCategory = $scope.categories[0];
                    }
                    console.log('get course type ', $scope.categories);

                }).error(function (e) {

                });
            // $scope.uploader.onCompleteAll = function () {
            //     console.info('onCompleteAll');
            //     $state.go('home.course');
            // };
            // $scope.uploader.onBeforeUploadItem = function (item) {
            //     console.info('onBeforeUploadItem', item);
            //     item.formData.push({name: $scope.name});
            //     item.formData.push({content: $scope.content});
            //     item.formData.push({category: $scope.category});
            //     var formatDate = ($scope.date.getFullYear()+"-"+$scope.date.getMonth()+"-"
            //         +$scope.date.getDay()+" "+$scope.date.getHours()+":"+
            //         $scope.date.getMinutes()+":"+$scope.date.getSeconds());
            //     item.formData.push({date: $scope.date});
            // };
            //var editor = UE.getEditor('container');
            $scope.submit = function () {
                console.log('create new course ', $scope.date);
                //$scope.uploader.uploadAll();
                var date= $scope.date;
                if($scope.date.getFullYear() !== null){
                    date = ($scope.date.getFullYear()+"-"+$scope.date.getMonth()+"-"
                    +$scope.date.getDay()+" "+$scope.date.getHours()+":"+
                     $scope.date.getMinutes()+":"+$scope.date.getSeconds());
                }
                //console.log('editor:',editor.getAllHtml());
                var req = {
                    method: 'POST',
                    url: 'http://' + $location.host() + ":" + $location.port() + '/education/zaozao/course/new',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
                    },
                    data: $httpParamSerializer({
                        name: $scope.name,
                        content: $scope.content,
                        category: $scope.category,
                        date: date
                    })
                };
                $http(req).success(function (e) {
                    console.log('edit success');
                    
                    $state.go('home.course');
                    
                }).error(function (e) {
                    console.log('edit failed ', e);
                });

            }
        }]);

    course.controller('CourseEditController', ['$scope', '$http', '$stateParams', '$state', '$location',
        'FileUploader', '$httpParamSerializer', 
        function ($scope, $http, $stateParams, $state, $location, FileUploader, $httpParamSerializer) {

            console.log('edit course id=', $stateParams.courseId);
            $scope.dateOptions = {
                startingDay: 1,
                showWeeks: true
            };
            $scope.status={};
            $scope.status.opened=false;
            $scope._simpleConfig = {
                 //这里可以选择自己需要的工具按钮名称,此处仅选择如下五个
                 toolbars: [
                   ['FullScreen', 'Source', 'Undo', 'Redo', 'Bold', 'simpleupload']
                 ],
                 //focus时自动清空初始化时的内容
                 autoClearinitialContent: true,
                 //关闭字数统计
                 wordCount: false,
                 //关闭elementPath
                 elementPathEnabled: false
               };

            $scope.open = function($event) {
                $scope.status.opened = true;
            };
            $http.get('http://' + $location.host() + ":" + $location.port() + '/education/zaozao/course/querycourse/' + $stateParams.courseId)
                .success(function (e) {
                    var json = JSON.parse(JSON.stringify(e));
                    $scope.course = json;
                    console.log('course:', $scope.course);
                    //console.log('content', $scope.course.content);
                    if($scope.course.picture_paths != null){
                        $scope.course.imageurl = 'http://' + $location.host() + ":" + $location.port() +
                            '/education/public/resources/courses/' + $scope.course.id + '/' + $scope.course.picture_paths;
                    }else{
                        $scope.course.imageurl = null;
                    }
                }).error(function (e) {
                    console.log('error:', e);
                    var ret = JSON.stringify(e);
                    var json = JSON.parse(ret);
                    if ('-10' === json.status) {
                        $state.go('home.login');
                    }
                });
            $scope.uploader = new FileUploader({
                url: 'http://' + $location.host() + ":" + $location.port() + '/education/zaozao/course/upload_resource',
                formData: []
            });

            $scope.submit = function () {
                console.log('edit course ', $scope.course.date);
                var date= $scope.course.date;
                if($scope.course.date.getFullYear() !== null){
                    date = ($scope.course.date.getFullYear()+"-"+$scope.course.date.getMonth()+"-"
                    +$scope.course.date.getDay()+" "+$scope.course.date.getHours()+":"+
                     $scope.course.date.getMinutes()+":"+$scope.course.date.getSeconds());
                }
                console.log('course date '+date);
                var req = {
                    method: 'POST',
                    url: 'http://' + $location.host() + ":" + $location.port() + '/education/zaozao/course/edit',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
                    },
                    data: $httpParamSerializer({
                        id: $scope.course.id,
                        name: $scope.course.name,
                        content: $scope.course.content,
                        category: $scope.course.category,
                        date: date
                    })
                };
                $http(req).success(function (e) {
                    console.log('edit success');
                    if($scope.uploader.queue.length == 0){
                        $state.go('home.course');
                    }
                }).error(function (e) {
                    console.log('edit failed ', e);
                });
                console.log("upload length "+$scope.uploader.queue.length );
                if($scope.uploader.queue.length > 0){
                    $scope.uploader.uploadAll();
                }

            }
            $scope.cancel = function () {
                $state.go('home.course');
            }

            $scope.uploader.onCompleteAll = function () {
                console.info('onCompleteAll');
                $state.go('home.course');
            };
            $scope.uploader.onBeforeUploadItem = function (item) {
                console.info('onBeforeUploadItem', item);
                item.formData.push({id: $scope.course.id});
                item.formData.push({name: $scope.course.name});
                item.formData.push({content: $scope.course.content});
                item.formData.push({category: $scope.course.category});
                item.formData.push({date: $scope.course.date});
            };
        }]);

    course.constructor("deleteCourse", ['$scope', '$http', '$state', '$location',
        function ($scope, $http, $state, $location) {
            $http.delete('http://' + $location.host() + ":" + $location.port() + '/education/zaozao/course')
                .success(function (e) {
                    console.log('delete success');
                }).error(function (e) {
                    console.log(e);
                });
        }]);
});