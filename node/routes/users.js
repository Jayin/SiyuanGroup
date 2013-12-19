var _ = require('underscore'),
	Users = require('../models/users'),
	User = Users.model;

module.exports = function (app) {
	// list users
	app.get('/api/users', function (req, res) {
		var defaultLimit = 10,
			offset, limit;
		if (_.has(req.query, 'limit')) {
			limit = ~~req.query['limit'];
		} else {
			limit = defaultLimit;
		}
		if (_.has(req.query, 'page')) {
			// TODO:
			// give out the page count
			// but not just adjusting
			var page = Math.max(1, ~~req.query['page']);
			offset = (page - 1) * defaultLimit;
		} else {
			offset = ~~req.query['offset'];
		}
		Users.forge().query()
			.offset(offset).limit(limit).select()
			.then(function (users) {
				res.send(users);
			});
	});

	// find user by id
	app.get('/api/users/:id', function (req, res) {
		var id = parseInt(req.params['id']);
		if (isNaN(id)) {
			res.send(new Error('invalid user id', -101));
			return;
		}
		User.forge({
			id: id
		}).fetch()
			.then(function (user) {
				if (user) {
					res.send(user);
				} else {
					res.send(new Error('no such user', -102));
				}
			});
	});
}