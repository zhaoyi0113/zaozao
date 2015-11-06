require.config({
    baseUrl: 'public/js',
    paths: {
        angular: '/education/bower_components/angular/angular.min',
        app: 'app',
        'ui-router': '/education/bower_components/angular-ui-router/release/angular-ui-router.min',
        home: 'home',
        'angular-file-upload': '/education/bower_components/angular-file-upload/dist/angular-file-upload.min'
    },
    shim: {
        'angular': {
            exports: 'angular'
        },
        'ui-router':{
            deps: ['angular']
        },
        'angular-file-upload':{
            exports: 'angular-file-upload'
        }
    }
});

require(['angular', 'app'], function() {
    angular.bootstrap(document, ['appModule']);
});


