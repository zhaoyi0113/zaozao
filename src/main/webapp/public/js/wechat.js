define(['angular'], function(angular) {
	var module = angular.module('wechatModule', []);
	module.controller('WeChatController', ['$scope', '$http', function($scope, $http) {
		$http.get('http://www.imzao.com/education/zaozao/wechat/jsapi')
			.success(function(e) {
				console.log(e);
				var signature = e;
				wx.config({
					debug: false,
					appId: e.appid,
					timestamp: e.timestamp,
					nonceStr: e.noncestr,
					signature: e.signature,
					jsApiList: ['onMenuShareTimeline', 'onMenuShareAppMessage']
				});
				wx.ready(function() {
					console.log('wx ready');
					
				});
				wx.error(function(res) {
					console.log('wx error');
				});
			}).error(function(e) {

			});
		wx.onMenuShareTimeline({
			title: ' 预教课程',
			link: 'http://www.imzao.com/article_detail_page_img.html#?id=78',
			imgUrl: 'http://www.imzao.com/resources/courses/images/1449377356554_pic5.JPG',
			success: function() {
				console.log('share success');
			},
			cancel: function() {
				console.log('cancel share');
			}
		});


		wx.onMenuShareAppMessage({
			title: 'title2', // 分享标题
			desc: 'bbb', // 分享描述
			link: 'http://www.imzao.com/article_detail_page_img.html#?id=78', // 分享链接
			imgUrl: 'http://www.imzao.com/resources/courses/images/1449377356554_pic5.JPG', // 分享图标
			// 分享类型,music、video或link，不填默认为link
			// 如果type是music或video，则要提供数据链接，默认为空
			success: function(res) {
				// 用户确认分享后执行的回调函数
				console.log('share success');
				alter('share success');
			},
			cancel: function(res) {
				// 用户取消分享后执行的回调函数
				console.log('cancel share');
				alter('share cancel');
			},
			fail: function(res) {
				alter('share faield ' + res);
			}
		});

		$scope.shareTimeLine = function() {
			wx.checkJsApi({
				jsApiList: [
					'onMenuShareTimeline',
					'onMenuShareAppMessage'
				],
				success: function(res) {
					//alert(JSON.stringify(res));
					console.log(JSON.stringify(res));
				}
			});

			//alert('已注册获取“发送给朋友”状态事件');
		}
	}]);
});