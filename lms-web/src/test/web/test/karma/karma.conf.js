'use strict';

// Karma configuration
// Generated on Sat Oct 05 2013 22:00:14 GMT+0700 (ICT)

module.exports = function(config) {
    var basePath = '../../../../../target/lms';

    config.set({

        // base path, that will be used to resolve files and exclude
        basePath: basePath,


        // frameworks to use
        frameworks: ['jasmine'],


        // list of files / patterns to load in the browser
        files: [
                'vendor/headjs/head.js',
                '../../src/test/web/test/karma/base.js',
                'js/common/i18n/labels.js',
                '../../src/test/web/test/karma/translate.js',
                'vendor/angular-file-upload/angular-file-upload-shim.js',
                'vendor/jquery/jquery-1.11.0.js',
                'vendor/jquery-ui/jquery-ui.min.js',
                'vendor/angular/angular.min.js',
                'vendor/angular/angular-route.js',
                'vendor/angular/angular-resource.js',
                'vendor/angular-file-upload/angular-file-upload.js',
                'vendor/angular-ui/ui-bootstrap.js',
                'vendor/angular-ui/ui-utils.js',
                'vendor/autonumeric/autonumeric.js',
                'vendor/timepicker-addon/jquery-ui-timepicker-addon.js',
                'vendor/ui-router/angular-ui-router.js',
                'vendor/angular-locale/angular-locale_pt_br.js',
                'vendor/chosen/chosen.jquery.js',
                'vendor/angular-chosen/chosen.js',
                'vendor/angular/angular-mocks.js',
                'vendor/moment/moment-with-langs.js',
                'vendor/jquery-blockui/jquery.blockUI.js',
                'vendor/toastr/toastr.min.js',

                'js/adsm-framework.js',
                'js/lms-app.js',
            
                '../../src/test/web/test/karma/unit/**/*.js'
        ],

        // list of files to exclude
        exclude: [

        ],


        // test results reporter to use
        // possible values: 'dots', 'progress', 'junit', 'growl', 'coverage'
        //reporters: ['progress'],
        reporters: ['progress', 'coverage'],

        // coverage
        preprocessors: {
            // source files, that you wanna generate coverage for
            // do not include tests or libraries
            // (these files will be instrumented by Istanbul)
            'js/adsm-framework.js': ['coverage'],
            'js/lms-app.js': ['coverage']
        },

        coverageReporter: {
            type: 'html',
            dir: '../../src/test/web/test/coverage/'
        },

        // web server port
        port: 9876,


        // enable / disable colors in the output (reporters and logs)
        colors: true,


        // level of logging
        // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
        logLevel: config.LOG_INFO,


        // enable / disable watching file and executing tests whenever any file changes
        autoWatch: true,


        // Start these browsers, currently available:
        // - Chrome
        // - ChromeCanary
        // - Firefox
        // - Opera
        // - Safari (only Mac)
        // - PhantomJS
        // - IE (only Windows)
        browsers: ['Chrome'],


        // If browser does not capture in given timeout [ms], kill it
        captureTimeout: 60000,


        // Continuous Integration mode
        // if true, it capture browsers, run tests and exit
        singleRun: true
    });
};