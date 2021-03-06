define(['angular', 'ui-router', 'jquery-ui', 'home', 'wechat', 'login_service','angular-gestures'], function(angular) {
    'use strict';

    console.log('app');
    var app = angular.module("appModule", ['ui.router', 'homeModule', 'wechatModule', 'loginServiceModule', 'angular-gestures']);

    app.config(function($stateProvider, $urlRouterProvider, hammerDefaultOptsProvider) {
        $stateProvider.state('home', {
            url: '/home',
            templateUrl: '/education/public/views/home.html',
            controller: 'HomeController'
        }).state('home.user', {
            url: '/user',
            templateUrl: '/education/public/views/userlist.html',
            controller: 'UserController'
        }).state('home.edit', {
            url: '/edit?userId',
            templateUrl: '/education/public/views/useredit.html',
            controller: 'UserEditController'
        }).state('home.newuser', {
            url: '/adduser',
            templateUrl: '/education/public/views/newuser.html',
            controller: 'NewUserController'
        }).state('home.login', {
            url: '/login?state',
            templateUrl: '/education/public/views/login.html',
            controller: 'LoginController'
        }).state('home.course', {
            url: '/course',
            templateUrl: '/education/public/views/course.html',
            controller: 'CourseController'
        }).state('home.newcourse', {
            url: '/newcourse',
            templateUrl: '/education/public/views/newcourse.html',
            controller: 'NewCourseController'
        }).state('home.courseedit', {
            url: '/courseedit?courseId',
            templateUrl: '/education/public/views/courseedit.html',
            controller: 'CourseEditController'
        }).state('home.courseplan', {
            url: '/courseplan',
            templateUrl: '/education/public/views/courseplan.html',
            controller: 'CoursePlanController'
        }).state('home.courseplan_new', {
            url: '/courseplan_new',
            templateUrl: '/education/public/views/courseplan_new.html',
            controller: 'CoursePlanNewController'
        }).state('home.courseplan_edit', {
            url: '/courseplan_edit?id',
            templateUrl: '/education/public/views/courseplan_edit.html',
            controller: 'CoursePlanEditController'
        }).state('wechat', {
            url: '/wechat',
            templateUrl: '/education/public/views/share_wechat.html',
            controller: 'WeChatController'
        }).state('home.coursetag', {
            url: '/coursetag',
            templateUrl: '/education/public/views/coursetag.html',
            controller: 'CourseTagController'
        }).state('home.coursetag_new', {
            url: '/coursetag_new',
            templateUrl: '/education/public/views/coursetag_new.html',
            controller: 'NewCourseTagController'
        }).state('home.coursetag_edit', {
            url: '/coursetag_edit?courseTagId',
            templateUrl: '/education/public/views/coursetag_edit.html',
            controller: 'EditCourseTagController'
        }).state('home.config', {
            url: '/config',
            templateUrl: '/education/public/views/homeconfig.html',
            controller: 'HomeConfigController'
        }).state('home.course_video',{
            url: '/course_video',
            templateUrl: '/education/public/views/course_video.html',
            controller: 'CourseVideoController'
        }).state('home.course_video_edit',{
            url: '/course_video_edit?courseId',
            templateUrl: '/education/public/views/course_video_edit.html',
            controller: 'CourseVideoEditController'
        }).state('home.course_analytics',{
            url:'/course_analytics',
            templateUrl: '/education/public/views/course_analytics.html',
            controller: 'CourseAnalyticsController'
        }).state('home.course_analystic_detail',{
            url: '/course_analytics_detail?courseId',
            templateUrl: '/education/public/views/course_analytics_detail.html',
            controller: 'CourseAnalyticsDetailController'
        }).state('home.homecourse',{
            url: '/homecourse',
            templateUrl: '/education/public/views/homecourse.html',
            controller: 'HomeCourseController'
        }).state('home.user_profile_privilege',{
            url: '/user_privilege',
            templateUrl: '/education/public/views/user_privilege.html',
            controller: 'UserPrivilegeController'
        }).state('home.online_user',{
            url: '/online_user',
            templateUrl: '/education/public/views/online_user_analytics.html',
            controller: 'OnlineUserAnalyticsController'
        }).state('home.online_user_detail',{
            url: '/online_user_detail?userId&userName',
            templateUrl: '/education/public/views/online_user_detail_analytics.html',
            controller: 'OnlineUserDetailAnalyticsController'
        });
        $urlRouterProvider.otherwise('/home');

        hammerDefaultOptsProvider.set({
            recognizers: [[Hammer.Swipe, {time: 250}]]
        });
    });

    app.run(['$rootScope', '$location', 'LoginService', '$state',
        function($rootScope, $location, loginSrv, $state) {
            $rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams) {
                console.log('state changed to ' + toState.name+'to params',toParams, ', from state '+fromState.name+", from params ",fromParams);
                if (toState.name === 'home.login') {
                    return; // 如果是进入登录界面则允许
                }
                loginSrv.isLogin().then(function(event){
                    console.log('alread login ', event);
                }, function(error){
                    event.preventDefault();
                    $state.go('home.login',{state: toState.name});
                });
            });
        }
    ]);
});