var path = require('path');

var rootDir = process.cwd(),
	viewDir = path.resolve(rootDir, 'views'),
	publicDir = path.resolve(rootDir, 'public'),
	imgDir = path.resolve(publicDir, 'img'),
	cssDir = path.resolve(publicDir, 'css'),
	jsDir = path.resolve(publicDir, 'js');

module.exports = {
	rootDir: rootDir,
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
		// `charset: "utf8"` removed
	}
}