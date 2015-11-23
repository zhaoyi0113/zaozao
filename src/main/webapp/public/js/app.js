define(['angular', 'ui-router', 'jquery-ui' , 'home'], function(angular){
    'use strict';

    console.log('app');
    var app = angular.module("appModule", ['ui.router', 'homeModule']);

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
        });
        $urlRouterProvider.otherwise('/home');
    });
});