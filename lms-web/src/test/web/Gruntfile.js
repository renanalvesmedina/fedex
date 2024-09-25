module.exports = function(grunt) {
	require('load-grunt-tasks')(grunt);
	require('time-grunt')(grunt);

	grunt.initConfig({
		pkg: grunt.file.readJSON('package.json'),
        karma: {
            unit: {
                configFile: 'test/karma/karma.conf.js'
            }
        }
    });
    
    grunt.registerTask('default', ['build']);
    
    //frontend test
    grunt.registerTask('test:frontend', ['karma:unit']);
};
