var spawn = require('child_process').spawn,
	assert = require('assert'),
	config = require('../config/'),
	rootDir = config.rootDir;

describe('server', function() {
	it('starts', function(done) {
		var server = spawn('node', [rootDir]);
		process.on('exit', function() {
			server.kill();
		});
		server.on('error', function(err) {
			assert.equal(err, null);
		});
		server.stdout.on('data', function(data) {
			if (/started/.test(data)) {
				done();
			}
		});
	});

	require('./models');
	require('./api');
});