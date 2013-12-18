var assert = require('assert'),
	request = require('request'),
	config = require('../config/'),
	apiHost = 'http://localhost:' + config.port + '/api';

describe('api', function() {
	describe('users', function() {
		describe('finds list', function() {
			var url = apiHost + '/users';
			it('all', function(done) {
				request(url, function(err, res, json) {
					assert.ok(JSON.parse(json).length > 0);
					done();
				});
			});
			// TODO
			/*it('with limit/skip', function(done) {
				request(url, {
					qs: {
						limit: 1,
						offset: 1
					}
				}, function(err, res, json) {
					done();
				});
			});*/
		});
		describe('finds one', function() {
			it('that exists', function(done) {
				var url = apiHost + '/users/2';
				request(url, function(err, res, json) {
					assert.equal(JSON.parse(json).id, 2);
					done();
				});
			});
			it('that does not exist', function(done) {
				var url = apiHost + '/users/-1';
				request(url, function(err, res, json) {
					var error = JSON.parse(json)
					assert.ok(error.request);
					assert.ok(error.errCode);
					assert.ok(error.errMsg);
					done();
				});
			});
		});
	});
});