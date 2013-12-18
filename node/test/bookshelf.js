var assert = require('assert'),
	Bookshelf = require('bookshelf'),
	_ = require('underscore'),
	chance = new(require('chance'))(),
	config = require('../config/'),
	dbConfig = config.db;

describe('bookshelf', function() {
	describe('mysql', function() {
		var MySql = Bookshelf.initialize(dbConfig);

		describe('model', function() {
			var User = MySql.Model.extend({
				tableName: 'users'
			});

			var id1;
			var name1 = '中文 ' + chance.integer();
			var user1 = new User({
				username: name1,
				password: '1234',
				email: chance.email()
			});
			var user2 = new User({
				username: '中文 ' + chance.integer(),
				password: '1234',
				email: chance.email()
			});

			it('sets/gets', function() {
				assert.equal(user1.get('password'), '1234');
				user1.set({
					password: '4567'
				});
				assert.equal(user1.get('password'), '4567');
			});

			it('saves', function(done) {
				user1.save().then(function() {
					assert.notEqual(user1.id, null);
					id1 = user1.id;
					user2.save().then(function() {
						assert.notEqual(user2.id, null);
						done();
					});
				});
			});

			it('fetches', function(done) {
				var user2 = new User({
					id: id1
				});
				user2.fetch().then(function() {
					assert.equal(user2.get('username'), name1);
					done();
				});
			});

			it('destroys', function(done) {
				user1.destroy().then(function() {
					var user3 = new User({
						username: name1
					});
					user3.fetch().then(function() {
						assert.equal(user3.id, null);
						done();
					});
				});
			});
		});
	});
});