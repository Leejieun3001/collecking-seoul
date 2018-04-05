var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

router.get('/oauth/login/kakao/callback', function () {
  console.log('hkhk');
});

module.exports = router;
