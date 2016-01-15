require.config({
    baseUrl: 'public/js',
    paths: {
        angular: '/education/bower_components/angular/angular.min',
        app: 'app',
        'ui-router': '/education/bower_components/angular-ui-router/release/angular-ui-router.min',
        home: 'home',
        'angular-file-upload': '/education/bower_components/angular-file-upload/dist/angular-file-upload.min',
        'angular-ui-date' : '/education/bower_components/angular-ui-date/src/date',
        'jquery-ui': '/education/bower_components/jquery-ui/jquery-ui.min',
        'angular-bootstrap': '/education/bower_components/angular-bootstrap/ui-bootstrap.min',
        'angular-ui-bootstrap-datetimepicker':'/education/bower_components/angular-ui-bootstrap-datetimepicker/datetimepicker',
        'angular-bootstrap-tpls': '/education/bower_components/angular-bootstrap/ui-bootstrap-tpls.min',
        'angular-editor' : '/education/bower_components/angular-ueditor/dist/angular-ueditor.min',
        'ueditor-config' : '/education/bower_components/ueditor/ueditor.config',
        'ueditor-all' : '/education/bower_components/ueditor/ueditor.all',
        'angular-route': '/education/bower_components/angular-route/angular-route.min',
        'kindeditor' : '/education/bower_components/angular-kindeditor/kindeditor/kindeditor-all-min',
        'kindeditor-zh' : '/education/bower_components/angular-kindeditor/kindeditor/lang/zh-CN',
        'angular-kindeditor' : '/education/bower_components/angular-kindeditor/src/angular-kindeditor',
        'angular-datepicker' : '/education/bower_components/angular-datepicker/dist/angular-datepicker',
        'wx' : 'http://res.wx.qq.com/open/js/jweixin-1.0.0.js',
        'angular-animate': '/education/bower_components/angular-animate/angular-animate.min',
        'tweenmax': '/education/bower_components/gsap/src/minified/TweenMax.min',
        'jquery': '/education/bower_components/jquery/dist/jquery.min',
        'ng-dialog': '/education/bower_components/ng-dialog/js/ngDialog.min',
        'hammerjs': '/education/bower_components/hammerjs/hammer.min',
        'angular-gestures': '/education/bower_components/angular-gestures/gestures.min',
        'bootstrap-carousel-swipe': '/education/bower_components/bootstrap-carousel-swipe/carousel-swipe',
        'bootstrap': '/education/bower_components/bootstrap/dist/js/bootstrap.min'
    },
    shim: {
        'angular': {
            exports: 'angular'
        },
        'ui-router':{
            deps: ['angular']
        },
        'angular-route':{
            deps: ['angular']
        },
        'angular-file-upload':{
            exports: 'angular-file-upload'
        },
        'jquery-ui':{
            deps: ['angular']
        },
        'angular-ui-date':{
            deps: ['angular']
        },
        'angular-file-upload':{
            deps: ['angular']
        },
        'angular-bootstrap':{
            deps: ['angular']
        },
        'angular-ui-bootstrap-datetimepicker':{
            deps: ['angular','angular-bootstrap','angular-bootstrap-tpls']
        },
        'angular-bootstrap-tpls':{
            deps: ['angular','angular-bootstrap']
        }
        ,
        'ueditor-all':{
            deps: ['ueditor-config']
        },
        // 'ueditor-config':{
        //     exports: ['ueditor-config']
        // },
        
        'angular-editor':{
            deps: ['angular','ueditor-all','ueditor-config']
        },
        'kindeditor' :{
            deps: ['jquery']
        },
        'kindeditor-zh':{
            deps: ['kindeditor'],
            exports: 'kindeditor-zh'
        },
        'angular-kindeditor':{
            deps: ['angular', 'kindeditor']
        },
        'angular-datepicker':{
            deps: ['angular'],
            exports: 'angular-datepicker'
        },
        'angular-animate': {
            deps: ['angular']
        },
        'ng-dialog': {
            deps: ['angular']
        },
        'angular-gestures':{
            deps: ['angular', 'hammerjs']
        },
        'bootstrap-carousel-swipe':{
            deps: ['bootstrap'],
            exports: 'bootstrap-carousel-swipe'
        },
        'angular-bootstrap': {
            deps: ['angular']
        }
    }
});

require(['angular', 'app'], function() {
    angular.bootstrap(document, ['appModule']);
});


