const pool = require('../config/dbPool');
const router = require('express').Router();
const bcrypt = require('bcrypt-nodejs');
const async = require('async');
const globalModule = require('../module/globalModule');
const errorConfig =  require('../config/error');
const aws = require('aws-sdk');
const multer = require('multer');
const multerS3 = require('multer-s3');
const jwtModule = require('../module/jwtModule');
aws.config.loadFromPath('./config/aws_config.json')
const s3 = new aws.S3();
const upload = multer({
  storage: multerS3({
    s3: s3,
    bucket: 'collecking-seoul',
    acl: 'public-read',
    key: function(req, file, cb) {
      cb(null, Date.now() + '.' + file.originalname.split('.').pop())
    }
  })
});

/* GET users listing. */
router.get('/', function(req, res, next) {
    res.status(200).send("hi");
});

/**
 * api 목적        : 토큰 갱신
 * request params : { idx: 유저 인덱스 }
 */
router.post('/refresh_token', function (req, res) {
    var resultJson = {
        token: '',
        message: ''
    };

    let checkValid = function (connection, callback) {
        if (req.body.idx == "" || req.body.idx == null) {
            res.status(200).send(errorConfig.NULL_VALUE);
            callback("ALREADY_SEND_MESSAGE", connection, "api : /user/refresh_token");
        } else {
            callback(null, connection);
        }
    }

    let selectUserInfo = function (connection, callback) {
        connection.query("select u.idx, u.id, u.nickname, u.phone, u.birth, u.sex, (case when p.board_idx is null then '' else p.url end) url "
                        + "from colleckingDB.User u "
                        + "left outer join colleckingDB.Photo p "
                        + "on u.idx=p.user_idx "
                        + "where u.idx = ?", req.body.idx, function (error, rows) {
            if (error) callback(error, connection, "Selecet query Error : ");
            else {
                if (rows.length === 0) {
                    // 존재하는 아이디가 없는 경우
                    res.status(200).send(errorConfig.NOT_SIGN_UP);
                    callback("ALREADY_SEND_MESSAGE", connection, "api : /user/refresh_token");
                } else {
                    callback(null, connection, rows[0]);
                }
            }
        });
    }

    let setResultInfo = function (connection, row, callback) {
        resultJson.message = "SUCCESS";
        resultJson.user = {
            idx: row.idx,
            id: row.id,
            nickname: row.nickname,
            phone: row.phone,
            birth: row.birth,
            url: row.url,
            sex: row.sex
        };
        console.log('login', row);
        resultJson.token = jwtModule.makeToken(row);
        res.status(200).send(resultJson);
        callback(null, connection, "api : /user/refresh_token");
    }

    var task = [globalModule.connect.bind(this), checkValid, selectUserInfo, setResultInfo, globalModule.releaseConnection.bind(this)];
    async.waterfall(task, globalModule.asyncCallback.bind(this));
});

/**
 * api 목적        : 프로필 사진 수정
 * request params : {photo: File}
 */
router.put('/update_photo', upload.single('photo'), function (req, res) {
  let resultJson = {
      message: ''
  };

  let checkToken = function (connection, callback) {
      var decodedToken = jwtModule.decodeToken(req.headers.token);
      if (!decodedToken.hasOwnProperty('token')) {
          res.status(200).send(decodedToken);
          callback("ALREADY_SEND_MESSAGE", connection, "api : /landmark/mine");
      } else {
          callback(null, connection, decodedToken.token.idx);
      }
  };

  let checkValid = function (connection, u_idx, callback) {
    if (req.file === undefined) {
      res.status(200).send(errorConfig.NO_IMAGE);
      callback("ALREADY_SEND_MESSAGE", connection, "api : /landmark/mine");
    } else {
        callback(null, connection, u_idx);
    }
  }

  let updatePhoto = function (connection, u_idx, callback) {
      let params = [
        req.file.location,
        u_idx
      ];
      connection.query('update Photo set url = ? where user_idx = ? and board_idx is null', 
          params, function (error, rows) {
          if (error) callback(error, connection, "Update query Error : ", res);
          else {
              resultJson.message = "SUCCESS";
              res.status(200).send(resultJson);
              callback(null, connection, "api : /landmark/mine");
          }
      });
  }

  var task = [globalModule.connect.bind(this), checkToken, checkValid, selectMyLandmark, globalModule.releaseConnection.bind(this)];
  async.waterfall(task, globalModule.asyncCallback.bind(this));
});

/**
 * api 목적        : 회원정보수정
 * request params : { 
 *                      string phone: "01040908370",
 *                      string nickname: "이름",
 *                      string birth: "19950825",
 *                      number sex: 0(남자) || 1(여자)
 *                  }
 */
router.put('/update_info', function (req, res) {
    let checkToken = function (connection, callback) {
        var decodedToken = jwtModule.decodeToken(req.headers.token);
        if (!decodedToken.hasOwnProperty('token')) {
            res.status(200).send(decodedToken);
            callback("ALREADY_SEND_MESSAGE", connection, "api : /user/update_info");
        } else {
            callback(null, connection, decodedToken.token.idx);
        }
    };

    let checkValid = function (connection, u_idx, callback) {
        let result = globalModule.checkBasicValid(req.body);
        if (result !== "OK") {
            res.status(200).send(result);
            callback("ALREADY_SEND_MESSAGE", connection, "api : /user/update_info");
        } else {
            callback(null, connection, u_idx);
        }
    }

    let updateUserInfo = function (connection, u_idx, callback) {
        let updateQuery = "update User set nickname= ?, phone = ?, birth = ?, sex = ? where idx = ?";
        let params = [
            req.body.nickname,
            req.body.phone,
            new Date(req.body.birth),
            req.body.sex,
            u_idx
        ];

        connection.query(updateQuery, params, function (err, data) {
            if (err) {
                callback(err, connection, "update query error : ", res);
            }
            else {
                res.status(200).send({message: "SUCCESS"});
                callback(null, connection, "api : /user/update_info");
            }
        });
    }

    var task = [globalModule.connect.bind(this), checkToken, checkValid, updateUserInfo, globalModule.releaseConnection.bind(this)];
    async.waterfall(task, globalModule.asyncCallback.bind(this));
});

/**
 * api 목적        : 비밀번호수정
 * request params : { 
 *                      string currentPW: "1111",
 *                      string newPW1: "1111",
 *                      string newPW2: "1111"
 *                  }
 */
router.put('/update_pw', function (req, res) {
    let checkToken = function (connection, callback) {
        var decodedToken = jwtModule.decodeToken(req.headers.token);
        if (!decodedToken.hasOwnProperty('token')) {
            res.status(200).send(decodedToken);
            callback("ALREADY_SEND_MESSAGE", connection, "api : /user/update_info");
        } else {
            callback(null, decodedToken.token.idx, connection);
        }
    };

    let checkValid = function (connection, u_idx, callback) {
        let result = globalModule.checkBasicValid(req.body);
        if (result !== "OK") {
            res.status(200).send(result);
            callback("ALREADY_SEND_MESSAGE", connection, "api : /user/update_pw");
        } else if (req.body.newPW1 !== req.body.newPW2) {
            res.status(200).send(errorConfig.NOT_CORRESPOND);
            callback("ALREADY_SEND_MESSAGE", connection, "api : /user/update_pw");
        }else {
            callback(null, u_idx, connection);
        }
    }

    let selectUserInfo = function (connection, u_idx, callback) {
        connection.query('select password from User where idx = ?', 
            u_idx, function (error, rows) {
            if (error) callback(error, connection, "Select query Error : ");
            else {
                bcrypt.compare(req.body.currentPW, rows[0].password, function (err, isCorrect) {
                    // isCorrect === true : 일치, isCorrect === false : 불일치
                    if (err) {
                        callback(err, connection, "Bcrypt Error : ", res);
                    } else {
                        if (!isCorrect) {
                            res.status(200).send(errorConfig.INCORRECT_PASSWORD);
                            callback("ALREADY_SEND_MESSAGE", connection, "api : /user/update_pw");
                        } else {
                            callback(null, connection, u_idx);
                        }
                    }
                });
            }
        });
    }

    let updatePassword = function (connection, u_idx, callback) {
        bcrypt.hash(req.body.newPW1, null, null, function (err, hash) {
            if (err) callback(err, connection, "Bcrypt hashing Error : ",res);
            else {
                connection.query("update User set password = ? where idx = ?", [hash, u_idx], function (err, data) {
                    if (err) {
                        callback(err, connection, "update query error : ", res);
                    }
                    else {
                        res.status(200).send({message: "SUCCESS"});
                        callback(null, connection, "api : /user/update_pw");
                    }
                });
            }
        });
    }

    var task = [globalModule.connect.bind(this), checkToken, checkValid, selectUserInfo, updatePassword, globalModule.releaseConnection.bind(this)];
    async.waterfall(task, globalModule.asyncCallback.bind(this));
});

/**
 * api 목적        : 회원탈퇴
 */
router.delete('/withdraw', function (req, res) {
    let resultJson = {
        message: ''
    };
  
    let checkToken = function (connection, callback) {
        var decodedToken = jwtModule.decodeToken(req.headers.token);
        if (!decodedToken.hasOwnProperty('token')) {
            res.status(200).send(decodedToken);
            callback("ALREADY_SEND_MESSAGE", connection, "api : /user/withdraw");
        } else {
            callback(null, connection, decodedToken.token.idx);
        }
    };
  
    let deleteUser = function (connection, u_idx, callback) {
        connection.query('update User set id = "탈퇴한 회원", nickname = "탈퇴한 회원", isMember = 0 where idx = ?', 
            u_idx, function (error, rows) {
            if (error) callback(error, connection, "Update query Error : ", res);
            else {
                resultJson.message = "SUCCESS";
                res.status(200).send(resultJson);
                callback(null, connection, "api : /user/withdraw");
            }
        });
    }
  
    var task = [globalModule.connect.bind(this), checkToken, deleteUser, globalModule.releaseConnection.bind(this)];
    async.waterfall(task, globalModule.asyncCallback.bind(this));
  });

module.exports = router;