var assert = require('assert'),
	mysql = require('mysql'),
	config = require('../config/');

describe('mysql', function() {
	it('connects', function(done) {
		var connection = mysql.createConnection(config.db);
		connection.connect();
		connection.query('SELECT 1 + 1 AS solution', function(err, rows, fields) {
			assert.equal(err, null);
			assert.equal(rows[0].solution, 1 + 1);
			done();
		});
		connection.end();
	});
});