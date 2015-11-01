require.config({
    baseUrl: 'public/js',
    paths: {
        angular: '/education/bower_components/angular/angular.min',
        app: 'app',
        'ui-router': '/education/bower_components/angular-ui-router/release/angular-ui-router.min',
        home: 'home'
    },
    shim: {
        'angular': {
            exports: 'angular'
        },
        'ui-router':{
            deps: ['angular']
        }
    }
});

require(['angular', 'app'], function() {
    angular.bootstrap(document, ['appModule']);
});


