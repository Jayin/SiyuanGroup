var _ = require('underscore'),
	Bookshelf = require('bookshelf'),
	config = require('../config/'),
	dbConfig = config.db
	syBookshelf = module.exports = Bookshelf.initialize(dbConfig),
	syModel = syBookshelf.Model,
	syCollection = syBookshelf.Collection;

syModel = syBookshelf.Model.extend({
	// TODO
	// see Ghost as reference
	tableName: '',
	fields: [],

	// throw an `error` if problem exists,
	// according to `validators`
	validate: function() {
		var attrs = this.attributes, err;
		for (var key in this.validators) {
			err = this.validators[key](attrs[key]);
			if (err) {
				return err;
			}
		}
	},
	validators: {}
});