module.exports = function(req, res, next) {
	res.send = (function(fn) {
		return function send(obj) {
			if (obj instanceof Error) {
				return fn.call(res, {
					request: req.url,
					errCode: obj.code,
					errMsg: obj.message
				});
			}
			return fn.apply(res, arguments);
		}
	})(res.send);
	next();
}