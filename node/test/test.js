var //spawn = require('child_process').spawn,
request = require('request'),
	assert = require('assert'),
	//server = spawn('node ../app.js'),
	url = 'http://localhost:8088';

/* server */
/*server.stdout.on('data', function(data) {
	console.log('server out: ' + data);
});
server.stderr.on('data', function(data) {
	console.log('server err: ' + data);
});
server.on('close', function(code) {
	console.log('server process exited with code ' + code);
});*/

/* test */
request(url, function(e, r, body) {
	assert.equal(e, null);
	assert.equal(body, 'hello world');
});
process.on('exit', function() {
	console.log('nice done');
});