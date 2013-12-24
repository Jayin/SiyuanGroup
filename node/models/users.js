var _ = require('underscore'),
	chance = new(require('chance'))(),
	syBookshelf = require('./base'),
	User, Users;

User = syBookshelf.Model.extend({
	tableName: 'users',

	initialize: function() {
		this.on('saving', this.saving, this);
	},

	saving: function() {
		if (!this.get('regtime')) {
			this.set({
				'regtime': new Date()
			});
		}
	},

	toJSON: function() {
		var attrs = _.clone(this.attributes);
		// to timestamp
		attrs.regtime = new Date(attrs.regtime).getTime();
		return attrs;
	}
}, {
	fields: ['id', 'username', 'password', 'email', 'regtime'],

	validators: {
		'username': function(val) {
			if (!val) {
				return new Error('invalid username', -101);
			}
		},
		'password': function(val) {
			if (!val) {
				return new Error('invalid password', -102);
			}
		},
		'email': function(val) {
			if (!val) {
				return new Error('invalid email', -103);
			}
		}
	},

	random: function() {
		return this.forge({
			username: chance.word(),
			password: chance.string(),
			email: chance.email()
		});
	}
});

Users = module.exports = syBookshelf.Collection.extend({
	model: User
}, {
	model: User
});