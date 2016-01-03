define(['angular', 'ng-dialog', 'login_service'], function(angular) {

	'use strict';
	var pwd = angular.module("adminPwdServiceModule", ['ngDialog', 'loginServiceModule']);

	pwd.service('AdminPwdService', function($q, $http, ngDialog, LoginService) {

		this.adminPwd = '';

		this.checkInput = function(adminPwd) {
			console.log('save password ');
			this.adminPwd = loginSrv.sha1(adminPwd);
			return true;
		};

		this.openPasswordDlg = function() {
			var that = this;
			var promise = ngDialog.open({
				template: 'public/views/password_input.html',
				name: 'InputPassword',
				showClose: false,
				closeByEscape: true,
				className: 'ngdialog-theme-default',
				controller: ['$scope', function($scope) {
					$scope.adminPwd = '';
					$scope.checkInput = function(adminPwd) {
						console.log('save password ');
						that.adminPwd = LoginService.sha1(adminPwd);
						return true;
					};
				}]
			});
			return promise.closePromise;
		};

	});

});