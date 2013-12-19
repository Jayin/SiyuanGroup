/**
 * Module dependencies.
 */

var path = require('path'),
	express = require('express'),
	lessMiddleware = require('less-middleware'),
	// overwrite `Error` to `XError`
	Error = global.Error = require('xerror'),
	apiErrSender = require('./lib/api-err-sender'),
	routes = require('./routes'),
	config = require('./config/'),
	port = config.port,
	secret = config.secret,
	viewDir = config.viewDir,
	publicDir = config.publicDir,
	app = express();

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
app.use(lessMiddleware({
	src: publicDir
}));
app.use(express.static(publicDir));

// development only
if ('development' == app.get('env')) {
	app.use(express.errorHandler());
}

// error sending with api
app.use('/api', apiErrSender);
// routes
routes(app);

// listen on port
app.listen(port, function() {
	console.log('server started at %d\n', port);
});