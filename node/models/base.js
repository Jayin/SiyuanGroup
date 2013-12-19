var _ = require('underscore'),
	Bookshelf = require('bookshelf'),
	config = require('../config/'),
	dbConfig = config.db
	syBookshelf = module.exports = Bookshelf.initialize(dbConfig),
	syModel = syBookshelf.Model,
	syCollection = syBookshelf.Collection;

syModel = syModel.extend({
	// TODO
	// see Ghost as reference
	tableName: '',

	// throw an `error` if problem exists,
	// according to `validators`
	validate: function() {
		var attrs = this.attributes,
			err;
		for (var key in this.validators) {
			err = this.validators[key](attrs[key]);
			if (err) {
				return err;
			}
		}
	}
}, {
	fields: [],
	validators: {}
});

syCollection = syCollection.extend({
	model: syModel
}, {
	model: syModel
});