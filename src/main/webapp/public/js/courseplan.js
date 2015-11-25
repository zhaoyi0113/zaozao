define(['angular'], function(angular){
	'user strict';
	var courseplan = angular.module("coursePlanModel", []);
    courseplan.controller('CoursePlanController',
    	 ['$scope', '$http', '$location', function($scope, $http, $location){
    	 console.log('course plan');
    	 $scope.headers=['Title','Sub Title', 'Content', 'Price'];

    	 $http.get('http://'+$location.host()+":"+$location.port()+'/education/zaozao/courseplan')
    	 	.success(function(e){
    	 		console.log('get courseplan ',e);
    	 		$scope.coursePlanList = e;
    	 	}).error(function(e){

    	 	});
    }]);
});