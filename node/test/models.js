var assert = require('assert'),
	_ = require('underscore'),
	chance = new(require('chance'))(),
	Promise = require('bluebird'),
	Users = require('../models/users'),
	User = Users.model;

describe('models', function() {
	describe('users', function() {
		var numUsers = 30,
			firstId,
			users;

		it('saves', function(done) {
			users = Users.forge();
			_.times(numUsers, function(i) {
				users.add(randomUser());
			});
			Promise.all(users.invoke('save')).then(function() {
				assert.ok(_.every(users.models, function(m) {
					return m.id;
				}));
				firstId = users.at(0).id;
				done();
			});
		});

		it('fetches', function(done) {
			var users1 = Users.forge();
			_.times(numUsers, function(i) {
				users1.add(User.forge({
					id: firstId + i
				}));
			});
			Promise.all(users1.invoke('fetch')).then(function() {
				assert.ok(_.every(users1.models, function(m) {
					return m.get('username');
				}));
				done();
			});
		});

		it('destroys', function(done) {
			Promise.all(users.invoke('destroy')).then(function() {
				assert.ok(_.every(users.models, function(m) {
					return ! m.id;
				}));
				done();
			});
		});
	});
});

function randomUser() {
	return User.forge({
		username: chance.name(),
		password: chance.word(),
		email: chance.email()
	});
}