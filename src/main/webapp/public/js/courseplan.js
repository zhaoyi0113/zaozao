define(['angular'], function(angular){
	'user strict';
	var courseplan = angular.module("coursePlanModel", []);
    courseplan.controller('CoursePlanController',
    	 ['$scope', '$http', '$location', function($scope, $http, $location){
    	 console.log('course plan');
    	 $scope.headers=['Title','Sub Title', 'Content', 'Price', 'Edit', 'Delete'];

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
		'$httpParamSerializer','$state',
         function($scope, $http, $location, $httpParamSerializer, $state){
			
            $scope.create = function(){
                var req = {
                    method: 'POST',
                    url: 'http://'+$location.host()+":"+$location.port()+'/education/zaozao/courseplan',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
                    },
                    data: $httpParamSerializer({ title: $scope.title,
                        sub_title: $scope.sub_title,
                        content: $scope.content,
                        price: $scope.price
                    })
                };
                $http(req).success(function(e){
                    console.log('create new course plan successfully, ',e);
                    $state.go('home.courseplan');
                }).error(function(e){
                    console.error('create course plan error,',e);
                });
            }

		}]);
    courseplan.controller('CoursePlanEditController', ['$scope', '$http', '$location',
        '$httpParamSerializer', '$stateParams', '$state',
        function($scope, $http, $location, $httpParamSerializer,$stateParams, $state){
            console.log('get course plan id '+$stateParams.id);
            $scope.id = $stateParams.id;

            $http.get('http://'+$location.host()+":"+$location.port()+
            '/education/zaozao/courseplan/'+$scope.id)
            .success(function(e){
                console.log('get course plan ',e);
                $scope.coursePlan = e;
            }).error(function(e){

            });

            $scope.save = function(){
                var req = {
                    method: 'POST',
                    url: 'http://'+$location.host()+":"+$location.port()+
                        '/education/zaozao/courseplan/edit',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
                    },
                    data: $httpParamSerializer({ 
                        id: $scope.id,
                        title: $scope.coursePlan.title,
                        sub_title: $scope.coursePlan.subTitle,
                        content: $scope.coursePlan.content,
                        price: $scope.coursePlan.price
                    })
                };
                $http(req).success(function(e){
                    console.log('save course plan successfully.',e);
                    $state.go('home.courseplan');
                }).error(function(e){
                    console.error('save course plan error ',e);
                })

            }
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