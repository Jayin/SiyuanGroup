var path = require('path');

var rootDir = process.cwd();
var viewDir = path.resolve(rootDir, 'views');
var publicDir = path.resolve(rootDir, 'public');
var imgDir = path.resolve(publicDir, 'img');
var cssDir = path.resolve(publicDir, 'css');
var jsDir = path.resolve(publicDir, 'js');

module.exports = {
	viewDir: viewDir,
	publicDir: publicDir,
	imgDir: imgDir,
	cssDir: cssDir,
	jsDir: jsDir,

	port: 8088,
	secret: 'bad',
	db: {
		host: 'localhost',
		user: 'root',
		password: 'root'
	}
}