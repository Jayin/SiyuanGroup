var Users = require('../models/users'),
	User = Users.model;

module.exports = function(app) {
	// list users
	app.get('/api/users', function(req, res) {
		var opt = {};
		opt.limit = ~~req.query.limit;
		opt.offset = ~~req.query.offset;
		Users.forge().fetch({
			limit: 1
		}).then(function(users) {
			res.send(users);
		});
	});

	// find user by id
	app.get('/api/users/:id', function(req, res) {
		var id = parseInt(req.params.id);
		if (isNaN(id)) {
			res.send(new Error('invalid user id', -101));
			return;
		}
		User.forge({
			id: id
		}).fetch().then(function(user) {
			if (user) {
				res.send(user);
			} else {
				res.send(new Error('no such user', -102));
			}
		});
	});
}