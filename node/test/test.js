var assert = require('assert'),
	request = require('request'),
	url = 'http://localhost:8088';

describe('test', function() {
	it('should pass', function(done) {
		request(url, function(e, r, body) {
			assert.equal(e, null);
			assert.equal(body, 'hello world');
			done();
		});
	});
});