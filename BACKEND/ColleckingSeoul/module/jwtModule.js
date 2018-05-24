const jwt = require('jsonwebtoken');
const jwtSecret = require('../config/secretKey');
const errorConfig =  require('../config/error');
const secretKey = jwtSecret.secret;

//JWT 토큰 설정값
const option = {
  algorithm : 'HS256', //토큰 암호화 방식
  expiresIn :  "7d"    //토큰의 유효기간
};
const payload = {
  idx : 0,
  id : ''
};

//토큰 발급하기
function makeToken (value) {
  payload.idx = value.idx || 0;
  payload.id = value.id || '';
  var token = jwt.sign(payload, secretKey, option);
  return token;
}

//토큰 해석하기
function decodeToken (token) {
    //decoded.token.idx 로 idx를 구하기
    var decodeInfo = {};
    if(!token) {
      decodeInfo = errorConfig.NOT_LOGIN;
    } else {
      try{
        let decoded = jwt.verify(token, secretKey);
        decodeInfo.token = decoded;
      } catch(e) {
        console.log(e.message);
        if (e.message == "jwt expired") {
          decodeInfo = errorConfig.EXPIRED_TOKEN;
        }
      }
    }
    return decodeInfo;
}

function checkExpired (token) {
  let decodedToken = jwt.verify(token, secretKey);
  let now = new Date().getTime();
  if (decodedToken.exp < now) { return false; } 
  else { return true; }
}

module.exports.makeToken = makeToken;
module.exports.decodeToken = decodeToken;
module.exports.checkExpired = checkExpired;