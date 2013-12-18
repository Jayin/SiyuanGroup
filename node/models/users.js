var _ = require('underscore'),
	syBookshelf = require('./base'),
	User, Users;

User = syBookshelf.Model.extend({
	tableName: 'users',
	fields: ['id', 'username', 'password', 'email', 'regtime'],

	toJSON: function() {
		var attrs = _.clone(this.attributes);
		// to timestamp
		attrs.regtime = new Date(attrs.regtime).getTime();
		return attrs;
	},

	validators: {
		'username': function(val) {
			if (! val) {
				return new Error('invalid username', -101);
			}
		},
		'password': function(val) {
			if (! val) {
				return new Error('invalid password', -102);
			}
		},
		'email': function(val) {
			if (! val) {
				return new Error('invalid email', -103);
			}
		}
	}
});

Users = module.exports = syBookshelf.Collection.extend({
	model: User
});
Users.model = User;