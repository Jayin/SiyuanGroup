var assert = require('assert'),
	request = require('request'),
	config = require('../config/'),
	host = 'http://localhost:' + config.port;

describe('server', function() {
	it('routes to index', function(done) {
		var url = host + '/';
		request(url, function(err, res, body) {
			assert.equal(res.statusCode, 200);
			done();
		});
	});

	it('routes to users', function(done) {
		var url = host + '/users';
		request(url, function(err, res, body) {
			assert.equal(res.statusCode, 200);
			done();
		});
	});
});