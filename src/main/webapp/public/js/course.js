define(['angular', 'angular-file-upload', 'directives', 'angular-ui-date', 'angular-bootstrap', 'angular-bootstrap-tpls',
    'ueditor-config', 'kindeditor', 'kindeditor-zh', 'login_service',
    'angular-kindeditor', 'angular-datepicker', 'ng-dialog', 'admin_pwd_service'
], function(angular) {
    'use strict';
    var course = angular.module("courseModule", ['angularFileUpload', 'ngThumbModel',
        'ui.bootstrap', 'ngKeditor', 'ngDialog', 'loginServiceModule', 'adminPwdServiceModule'
    ]);

    course.controller('CourseController', ['$scope', '$http', '$location', '$state', 'ngDialog',
        'LoginService', 'AdminPwdService',
        function($scope, $http, $location, $state, $ngDialog, loginSrv, adminPwdSrv) {
            console.log("window.location:" + window.location.protocol);
            $scope.headers = ['Name', 'Publish Date', 'Status', 'Delete'];
            $http.get('http://' + $location.host() + ":" + $location.port() + '/education/zaozao/course/queryall')
                .success(function(e) {
                    var str = JSON.stringify(e);
                    var json = JSON.parse(str);
                    console.log('get all course ', json);
                    $scope.courses = json;
                }).error(function(e) {

                });
            $scope.newCourse = function() {
                $state.go('home.newcourse')
            }

            $scope.delete = function(id) {
                loginSrv.isLogin().then(function(event) {
                    console.log(event === 'admin');
                    if (event !== 'admin') {
                        adminPwdSrv.openPasswordDlg().then(function(data) {
                            console.log('close dialog ', data.value, adminPwdSrv.adminPwd);
                            if (data.value !== true) {
                                return;
                            }
                            $http.delete('http://' + $location.host() + ":" + $location.port() +
                                '/education/zaozao/course/' + id, {
                                    headers: {
                                        'password': adminPwdSrv.adminPwd
                                    }
                                }).success(function(e) {
                                deleteCourse(id);
                            }).error(function(e) {
                                alert('delete failed,');
                            });
                        });
                    } else {
                        $http.delete('http://' + $location.host() + ":" + $location.port() +
                            '/education/zaozao/course/' + id).success(function(e) {
                            deleteCourse(id);
                        });
                    }
                }, function(error) {

                });
            };

            function deleteCourse(id) {

                console.log('delete success');
                var i = 0;
                var course = null;
                for (i = 0; i < $scope.courses.length; i++) {
                    if (id === $scope.courses[i].id) {
                        course = $scope.courses[i];
                        break;
                    }
                }
                if (course !== null) {
                    $scope.courses.splice(i, 1);
                }

            }
        }
    ]);

    course.controller('NewCourseController', ['$scope', '$http', '$location', '$state', 'FileUploader',
        '$httpParamSerializer',
        function($scope, $http, $location, $state, FileUploader, $httpParamSerializer) {
            $scope.uploader = new FileUploader({
                url: 'http://' + $location.host() + ":" + $location.port() + '/education/zaozao/course/create',
                formData: []
            });
            $scope.kindeditor_config = {
                width: '100px',
                uploadJson: 'http://' + $location.host() + ":" + $location.port() +
                    '/education/zaozao/fileupload',
                allowFileManager: true,
                allowUpload: true,
                fileManagerJson: 'http://' + $location.host() + ":" + $location.port() +
                    '/education/zaozao/fileupload/manager',
                formatUploadUrl: false
            };
            $scope.status = {};
            $scope.status.opened = false;
            $scope.cstatus = true;
            var now = new Date();
            $scope.publishDate = now.getFullYear() + "/" + (now.getMonth() + 1) + "/" + now.getDate();
            console.log('now:', $scope.publishDate);
            // $scope._simpleConfig = {
            //      //这里可以选择自己需要的工具按钮名称,此处仅选择如下五个
            //      toolbars: [
            //        ['FullScreen', 'Source', 'Undo', 'Redo', 'Bold', 'simpleupload']
            //      ],
            //      //focus时自动清空初始化时的内容
            //      autoClearinitialContent: true,
            //      //关闭字数统计
            //      wordCount: false,
            //      //关闭elementPath
            //      elementPathEnabled: false
            //    };
            $scope.open = function($event) {
                $scope.status.opened = true;
            };
            $scope.dateOptions = {
                startingDay: 1,
                showWeeks: true
            };
            $http.get('http://' + $location.host() + ":" + $location.port() + '/education/zaozao/course_tags')
                .success(function(e) {
                    var str = JSON.stringify(e);
                    var json = JSON.parse(str);
                    $scope.tags = json;

                    console.log('get course tags ', $scope.tags);

                }).error(function(e) {

                });

            $scope.submit = function() {
                console.log('create new course ', $scope.publishDate);
                //$scope.uploader.uploadAll();
                // $scope.formatedDate = $scope.date;
                // if($scope.date.getFullYear() !== null){
                //     $scope.formatedDate = ($scope.date.getFullYear()+"-"
                //         +($scope.date.getMonth()+1)+"-"
                //     +$scope.date.getDate()+" "+$scope.date.getHours()+":"+
                //      $scope.date.getMinutes()+":"+$scope.date.getSeconds());
                // }
                //console.log('editor:',editor.getAllHtml());
                // var req = {
                //     method: 'POST',
                //     url: 'http://' + $location.host() + ":" + $location.port() + '/education/zaozao/course/new',
                //     headers: {
                //         'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
                //     },
                //     data: $httpParamSerializer({
                //         name: $scope.name,
                //         content: $scope.content,
                //         category: $scope.category,
                //         date: $scope.formatedDate,
                //         introduction: $scope.introduction
                //     })
                // };
                // $http(req).success(function (e) {
                //     console.log('edit success');

                //     $state.go('home.course');

                // }).error(function (e) {
                //     console.log('edit failed ', e);
                // });

                $scope.courseTags = '';
                for (var i = 0; i < $scope.tags.length; i++) {
                    console.log('tag value:' + $scope.tags[i].value);
                    if ($scope.tags[i].value === false || $scope.tags[i].value === undefined) {
                        continue;
                    }
                    $scope.courseTags += $scope.tags[i].id;
                    if (i != $scope.tags.length - 1) {
                        $scope.courseTags += ",";
                    }
                }
                if ($scope.cstatus === true) {
                    $scope.courseStatus = 'ENABLED';
                } else {
                    $scope.courseStatus = 'DISABLED';
                }
                $scope.uploader.uploadAll();
            }

            $scope.cancel = function() {
                $state.go('home.course');
            }
            $scope.uploader.onCompleteAll = function() {
                console.info('onCompleteAll');
                $state.go('home.course');
            };
            $scope.uploader.onBeforeUploadItem = function(item) {
                console.info('onBeforeUploadItem', item);
                item.formData.push({
                    name: $scope.name
                });
                item.formData.push({
                    content: $scope.content
                });
                item.formData.push({
                    introduction: $scope.introduction
                });
                item.formData.push({
                    tags: $scope.courseTags
                });
                item.formData.push({
                    publish_date: $scope.publishDate,
                    status: $scope.courseStatus,
                    video_external_url: $scope.videoUrl,
                    video_length: $scope.videoLength
                })
            };
            $scope.uploader.onErrorItem = function(fileItem, response, status, headers) {
                console.info('onErrorItem', fileItem, response, status, headers);
                console.error('status:', headers);
            };
        }
    ]);

    var editContent;
    course.controller('CourseEditController', ['$scope', '$http', '$stateParams', '$state', '$location',
        'FileUploader', '$httpParamSerializer',
        function($scope, $http, $stateParams, $state, $location, FileUploader, $httpParamSerializer) {

            console.log('edit course id=', $stateParams.courseId);
            $scope.dateOptions = {
                startingDay: 1,
                showWeeks: true
            };
            $scope.kindeditor_config = {
                width: '100px',
                uploadJson: 'http://' + $location.host() + ":" + $location.port() +
                    '/education/zaozao/fileupload',
                allowFileManager: true,
                allowUpload: true,
                fileManagerJson: 'http://' + $location.host() + ":" + $location.port() +
                    '/education/zaozao/fileupload/manager',
                formatUploadUrl: false,
                items: [
                    'source', '|', 'undo', 'redo', '|', 'preview', 'print', 'template', 'code', 'cut', 'copy', 'paste',
                    'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
                    'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
                    'superscript', 'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen', '/',
                    'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
                    'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image', 'multiimage',
                    'flash', 'media', 'insertfile', 'table', 'hr', 'emoticons', 'baidumap', 'pagebreak',
                    'anchor', 'link', 'unlink', '|', 'about'
                ]
            };
            $scope.status = {};
            $scope.status.opened = false;


            $scope.open = function($event) {
                $scope.status.opened = true;
            };
            $http.get('http://' + $location.host() + ":" + $location.port() + '/education/zaozao/course_tags')
                .success(function(e) {
                    $scope.courseTags = e;
                    console.log('course tags ', e);
                    for (var j = 0; j < $scope.courseTags.length; j++) {
                        $scope.courseTags[j].value = false;
                        if ($scope.course.tags !== null && $scope.course.tags !== undefined) {
                            for (var i = 0; i < $scope.course.tags.length; i++) {
                                for (var j = 0; j < $scope.courseTags.length; j++) {
                                    if ($scope.course.tags[i].name === $scope.courseTags[j].name) {
                                        $scope.courseTags[j].value = true;
                                    }
                                }
                            }
                        }
                    }

                }).error(function(e) {

                });
            $http.get('http://' + $location.host() + ":" + $location.port() + '/education/zaozao/course/querycourse/' + $stateParams.courseId)
                .success(function(e) {
                    var json = JSON.parse(JSON.stringify(e));
                    $scope.course = json;
                    console.log('course:', $scope.course);
                    //console.log('content', $scope.course.content);
                    console.log('path:', $location.path());
                    if ($scope.course.titleImageUrl != null) {
                        $scope.course.imageurl = $scope.course.titleImageUrl;
                    } else {
                        $scope.course.imageurl = null;
                    }
                    if ($scope.courseTags !== null && $scope.courseTags !== undefined) {
                        for (var i = 0; i < $scope.course.tags.length; i++) {
                            for (var j = 0; j < $scope.courseTags.length; j++) {
                                if ($scope.course.tags[i].name === $scope.courseTags[j].name) {
                                    $scope.courseTags[j].value = true;
                                }
                            }
                        }
                    }
                    if ($scope.course.status === 'ENABLED') {
                        $scope.course.cstatus = true;
                    } else {
                        $scope.course.cstatus = false;
                    }
                    editContent.cmd.inserthtml($scope.course.content);
                }).error(function(e) {
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

            $scope.submit = function() {
                console.log('edit course ', $scope.course.date);
                // $scope.formatedDate = $scope.course.date;
                // if($scope.course.date.getFullYear !== undefined){
                //     $scope.formatedDate = ($scope.course.date.getFullYear()+"-"
                //         +($scope.course.date.getMonth()+1)+"-"
                //     +$scope.course.date.getDate()+" "+$scope.course.date.getHours()+":"+
                //      $scope.course.date.getMinutes()+":"+$scope.course.date.getSeconds());
                // }
                if ($scope.course.cstatus === true) {
                    $scope.course.courseStatus = 'ENABLED';
                } else {
                    $scope.course.courseStatus = 'DISABLED';
                }
                $scope.courseTagId = '';
                for (var i = 0; i < $scope.courseTags.length; i++) {
                    if ($scope.courseTags[i].value === true) {
                        $scope.courseTagId += $scope.courseTags[i].id;
                        if (i !== $scope.courseTags.length - 1) {
                            $scope.courseTagId += ',';
                        }
                    }
                }
                if ($scope.uploader.queue.length == 0) {
                    var req = {
                        method: 'POST',
                        url: 'http://' + $location.host() + ":" + $location.port() + '/education/zaozao/course/edit',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
                                //'Content-Type': 'multipart/form-data; charset=utf-8;'
                        },
                        data: $httpParamSerializer({
                            id: $scope.course.id,
                            name: $scope.course.name,
                            content: $scope.course.content,
                            status: $scope.course.courseStatus,
                            publish_date: $scope.course.publishDate,
                            introduction: $scope.course.introduction,
                            tags: $scope.courseTagId,
                            video_external_url: $scope.course.videoUrl,
                            video_length: $scope.course.videoLength
                        })
                    };
                    $http(req).success(function(e) {
                        console.log('edit success');
                        $state.go('home.course');

                    }).error(function(e) {
                        console.log('edit failed ', e);
                    });
                } else {
                    console.log("upload length " + $scope.uploader.queue.length);

                    $scope.uploader.uploadAll();
                }

            }
            $scope.cancel = function() {
                $state.go('home.course');
            }

            $scope.uploader.onCompleteAll = function() {
                console.info('onCompleteAll');
                $state.go('home.course');
            };
            $scope.uploader.onBeforeUploadItem = function(item) {
                console.info('onBeforeUploadItem', item);
                item.formData.push({
                    id: $scope.course.id
                });
                item.formData.push({
                    name: $scope.course.name
                });
                item.formData.push({
                    introduction: $scope.course.introduction
                });
                item.formData.push({
                    content: $scope.course.content
                });
                item.formData.push({
                    tags: $scope.courseTagId,
                    status: $scope.course.courseStatus,
                    publish_date: $scope.course.publishDate,
                    video_external_url: $scope.course.videoUrl,
                    video_length: $scope.course.videoLength
                });
            };
        }
    ]);

    course.constructor("deleteCourse", ['$scope', '$http', '$state', '$location',
        function($scope, $http, $state, $location) {

            // $http.delete('http://' + $location.host() + ":" + $location.port() + '/education/zaozao/course')
            //     .success(function(e) {
            //         console.log('delete success');
            //     }).error(function(e) {
            //         console.log(e);
            //     });
        }
    ]);

    course.directive('coursekeditor', function() {

        var linkFn = function(scope, elm, attr, ctrl) {

            if (typeof KindEditor === 'undefined') {
                console.error('Please import the local resources of kindeditor!');
                return;
            }

            var _config = {
                width: '100%',
                autoHeightMode: false,
                afterCreate: function() {
                    this.loadPlugin('autoheight');
                }
            };

            var editorId = elm[0],
                editorConfig = scope.config || _config;

            editorConfig.afterChange = function() {
                if (!scope.$$phase) {
                    ctrl.$setViewValue(this.html());
                    // exception happens here when angular is 1.2.28
                    // scope.$apply();
                }
            };
            var keditor;
            if (KindEditor) {
                keditor = KindEditor.create(editorId, editorConfig);
                editContent = keditor;
                // console.log('watch ', attr.coursekeditor);
                // keditor.cmd.inserthtml(editContent);
                // scope.$watch(editContent, function(value){
                //     console.log('new value ',value);
                //     //keditor.cmd.inserthtml(value);
                // });
            }

            ctrl.$parsers.push(function(viewValue) {
                ctrl.$setValidity('keditor', viewValue);
                return viewValue;
            });
        };

        return {
            require: 'ngModel',
            scope: {
                config: '=config'
            },
            link: linkFn
        };
    });
});