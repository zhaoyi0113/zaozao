define(['angular', 'ui-router', 'jquery-ui' , 'home', 'wechat'], function(angular){
    'use strict';

    console.log('app');
    var app = angular.module("appModule", ['ui.router', 'homeModule','wechatModule']);

    app.config(function($stateProvider, $urlRouterProvider){
        $stateProvider.state('home',{
            url: '/home',
            templateUrl: '/education/public/views/home.html',
            controller: 'HomeController'
        }).state('home.user', {
            url: '/user',
            templateUrl: '/education/public/views/userlist.html',
            controller: 'UserController'
        }).state('home.edit',{
            url:  '/edit?userId',
            templateUrl: '/education/public/views/useredit.html',
            controller: 'UserEditController'
        }).state('home.adduser',{
            url: '/adduser',
            templateUrl: '/education/public/views/newuser.html',
            controller: 'NewUserController'
        }).state('home.login', {
            url: '/login',
            templateUrl: '/education/public/views/login.html',
            controller: 'LoginController'
        }).state('home.course',{
            url: '/course',
            templateUrl: '/education/public/views/course.html',
            controller: 'CourseController'
        }).state('home.newcourse',{
            url: '/newcourse',
            templateUrl: '/education/public/views/newcourse.html',
            controller: 'NewCourseController'
        }).state('home.courseedit',{
            url: '/courseedit?courseId',
            templateUrl: '/education/public/views/courseedit.html',
            controller: 'CourseEditController'
        }).state('home.courseplan',{
            url: '/courseplan',
            templateUrl: '/education/public/views/courseplan.html',
            controller: 'CoursePlanController'
        }).state('home.courseplan_new',{
            url: '/courseplan_new',
            templateUrl: '/education/public/views/courseplan_new.html',
            controller:'CoursePlanNewController'
        }).state('home.courseplan_edit',{
            url: '/courseplan_edit?id',
            templateUrl: '/education/public/views/courseplan_edit.html',
            controller:'CoursePlanEditController'
        }).state('wechat',{
            url: '/wechat',
            templateUrl: '/education/public/views/share_wechat.html',
            controller: 'WeChatController'
        }).state('home.coursetag',{
            url: '/coursetag',
            templateUrl: '/education/public/views/coursetag.html',
            controller: 'CourseTagController'
        }).state('home.coursetag_new',{
            url: '/coursetag_new',
            templateUrl: '/education/public/views/coursetag_new.html',
            controller: 'NewCourseTagController'
        }).state('home.coursetag_edit',{
            url: '/coursetag_edit?courseTagId',
            templateUrl: '/education/public/views/coursetag_edit.html',
            controller: 'EditCourseTagController'
        });
        $urlRouterProvider.otherwise('/home');
    });
});