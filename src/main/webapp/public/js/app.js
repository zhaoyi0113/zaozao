define(['angular', 'ui-router', 'home'], function(angular){
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
            controller: 'UserController'
        });
        $urlRouterProvider.otherwise('/home');
    });
});