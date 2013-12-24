var assert = require('assert'),
	_ = require('underscore'),
	request = require('request'),
	config = require('../../config/'),
	apiHost = 'http://localhost:' + config.port + '/api';

describe('api', function () {
	describe('users', function () {
		// assuming that there are
		// 200+ testing records
		describe('finds list', function () {
			var url = apiHost + '/users';
			it('with offset/limit', function (done) {
				request(url, {
					qs: {
						offset: 50,
						limit: 5
					}
				}, function (err, res, json) {
					var users = JSON.parse(json);
					assert.equal(users.length, 5);
					done();
				});
			});
			it('with page/limit', function (done) {
				request(url, {
					qs: {
						page: 3,
						limit: 7
					}
				}, function (err, res, json) {
					var users = JSON.parse(json);
					assert.equal(users.length, 7);
					done();
				});
			});
		});
		describe('finds one', function () {
			it('that exists', function (done) {
				var url = apiHost + '/users/2';
				request(url, function (err, res, json) {
					var user = JSON.parse(json);
					assert.equal(user.id, 2);
					done();
				});
			});
			it('that does not exist', function (done) {
				var url = apiHost + '/users/-1';
				request(url, function (err, res, json) {
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