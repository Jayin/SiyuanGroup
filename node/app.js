var express = require('express'),
	app = express();

app.get('/', function(req, res){
	res.send('hello world');
});

app.listen(8088, function(){
	console.log('server started');
});