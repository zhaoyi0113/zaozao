define(['angular'], function(angular){
	'user strict';
	var courseplan = angular.module("coursePlanModel", []);
    courseplan.controller('CoursePlanController',
    	 ['$scope', '$http', '$location', function($scope, $http, $location){
    	 console.log('course plan');
    	 $scope.headers=['Title','Sub Title', 'Content', 'Price'];

    	 getCoursePlanList($http, $scope, $location);

    	 $scope.delete = function(id){
    	 	$http.delete('http://'+$location.host()+":"+$location.port()+
    	 		'/education/zaozao/courseplan/'+id)
    	 		.success(function(e){
    	 			console.log('delete success.');
    	 			getCoursePlanList($http, $scope, $location);
    	 		}).error(function(e){
    	 			console.error('delete failed:'+id);
    	 		})
    	 }

    	 
    }]);

    courseplan.controller('CoursePlanNewController', ['$scope', '$http', '$location',
		function($scope, $http, $location){
			
		}]);

    var getCoursePlanList = function($http, $scope, $location){
    	 	$http.get('http://'+$location.host()+":"+$location.port()+
    	 	'/education/zaozao/courseplan')
    	 	.success(function(e){
    	 		console.log('get courseplan ',e);
    	 		$scope.coursePlanList = e;
    	 	}).error(function(e){

    	 	});
    	 }
});