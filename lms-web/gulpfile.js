var gulp = require('gulp');
var jshint = require('gulp-jshint');
var uglify = require('gulp-uglify');
var concat = require('gulp-concat');
var minify = require('gulp-minify');
var inject = require('gulp-inject');
var cssmin = require('gulp-cssmin');

var fs = require("fs");

var base_dir = './src/main/webapp/'
var destino_base = './target/';
var destino_js   = destino_base + 'lms/js/';
var destino_css   = destino_base + 'lms/css/';
var destino_html = destino_base + 'tmpMinify/WEB-INF/view/';

function getFilesArray(file) {
    var fileContent = fs.readFileSync(file, "utf8");
	fileContent = fileContent.substring(fileContent.indexOf("=")+1, fileContent.length);
	fileContent = eval(fileContent);

	for (var i = 0; i < fileContent.length; i++) {
		fileContent[i] = base_dir + fileContent[i];
	}

	return fileContent;
}

gulp.task('min_lms', function() {
    var fileContent = getFilesArray(base_dir + "js/lmsJs.js");
	
    gulp.src(fileContent)
	 .pipe(jshint())
	 .pipe(jshint.reporter('default'))
	 .pipe(concat('lms-app.js'))
	 .pipe(minify().on('error', function(e){
            console.log(e);
         }))
	 .pipe(gulp.dest(destino_js));
	
});

gulp.task('min_lms_css', function() {
    var fileContent = getFilesArray(base_dir + "js/lmsCss.js");
	
	gulp.src(fileContent)
	 .pipe(concat('lms-app.css'))
     .pipe(cssmin())
	 .pipe(gulp.dest(destino_css));
	
});

gulp.task('min_login', function() {
    var fileContent = getFilesArray(base_dir + "js/loginJs.js");

	gulp.src(fileContent)
	 .pipe(jshint())
	 .pipe(jshint.reporter('default'))
	 .pipe(uglify())
	 .pipe(concat('lms-login.js'))
	 .pipe(minify())
	 .pipe(gulp.dest(destino_js));
	
});

gulp.task('inj', function () {
	var target = gulp.src([base_dir + 'WEB-INF/view/login.html', base_dir + 'WEB-INF/view/index.html']);
    var transform = function (filePath, file) {
		  return file.contents.toString('utf8');
		};
	
	target
	.pipe(inject(gulp.src(['./gulpTemplate/LoginProdScript.html']), {
		starttag: '<!-- inject:body:login -->',
		transform: transform
	}))
    .pipe(inject(gulp.src(['./gulpTemplate/LmsProdScript.html']), {
		starttag: '<!-- inject:body:index -->',
		transform: transform
	}))
    .pipe(inject(gulp.src(['./gulpTemplate/LmsProdStyle.html']), {
		starttag: '<!-- inject:head:css -->',
		transform: transform
	}))
	.pipe(gulp.dest(destino_html));
 
});


gulp.task('default', ['min_lms', 'min_login', 'min_lms_css', 'inj']);
