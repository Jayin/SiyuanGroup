/**
 * Module dependencies.
 */

var path = require('path');
var express = require('express');
var lessMiddleware = require('less-middleware');
var routes = require('./routes');
var users = require('./routes/users');
var app = express();

var config = require('./config/');
var port = config.port;
var secret = config.secret;
var viewDir = config.viewDir;
var publicDir = config.publicDir;

// all environments
app.set('views', viewDir);
app.set('view engine', 'jade');
app.use(express.favicon());
app.use(express.logger('dev'));
app.use(express.json());
app.use(express.urlencoded());
app.use(express.methodOverride());
app.use(express.cookieParser(secret));
app.use(express.session());
app.use(app.router);
app.use(lessMiddleware({
	src: publicDir
}));
app.use(express.static(publicDir));

// development only
if ('development' == app.get('env')) {
	app.use(express.errorHandler());
}

// routers
app.get('/', routes.index);
app.get('/users', users.list);

// listen on port
app.listen(port, function() {
	console.log('server started at %d\n', port);
});