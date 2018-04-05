const mysql = require('mysql');
const pool = require('../config/dbPool');
const express = require('express');
const router = express.Router();
const bcrypt = require('bcrypt-nodejs');
const async = require('async');
const jwtModule = require('../module/jwtModule');
const globalModule = require('../module/globalModule');
const nodemailer = require('nodemailer');
const mailConfig = require('../config/mailAccount');
const errorConfig =  require('../config/error');
const secret =  require('../config/secretKey');

var loginByThirdparty = function (info, done) {
    console.log('process : ' + info.auth_type);
    var query = 'select * from User where id = ?';
    connection.query(query, info.auth_id, function (err, result) {
      if (err) {
        return done(err);
      } else {
        if (result.length === 0) {
          // 신규 유저는 회원 가입 이후 로그인 처리
          query = 'insert into User set id = ?, pass`nickname`= ?';
          connection.query(query, [info.auth_id, info.auth_name], function (err, result) {
            if(err){
              return done(err);
            }else{
              done(null, {
                'user_id': info.auth_id,
                'nickname': info.auth_name
              });
            }
          });
        } else {
          //기존유저 로그인 처리
          console.log('Old User');
          done(null, {
            'user_id': result[0].user_id,
            'nickname': result[0].nickname
          });
        }
      }
    });
  }

/**
 * api 목적        : 로그인 (일반)
 * request params : {string id: "아이디", string spassword: "비밀번호"}
 */
router.post('/', function (req, res) {
    let resultJson = {
        message: '',
        code:"",
        user: null
    };

    let checkValid = function (connection, callback) {
        let result = globalModule.checkBasicValid(req.body);
        if (result !== "FINE") {
            resultJson.code = result.code;
            resultJson.message = result.message;
            res.status(200).send(resultJson);
            callback("ALREADY_SEND_MESSAGE", connection, "api : /login/");
        } else {
            callback(null, connection, "api : /login/");
        }
    }

    let selectUserInfo = function (connection, callback) {
        connection.query('select * from User where id = ?', req.body.id, function (error, rows) {
            if (error) {
                callback(error, connection, "Selecet query Error : ");
            } else {
                callback(null, connection, rows);
            }
        });
    }

    var onSelectComlete = function (connection, rows, callback) {
        if (rows.length === 0) {
            // 존재하는 아이디가 없는 경우
            console.log("존재하는 아이디 없음");
            resultJson = errorConfig.NO_INFO;
            res.status(200).send(resultJson);
            callback("ALREADY_SEND_MESSAGE", connection, "api : /login/");
        } else {
            console.log("입력한 비밀번호" + req.body.password);
            console.log("DB 에 저장된 " + rows[0].password);

            bcrypt.compare(req.body.password, rows[0].password, function (err, isCorrect) {
                // isCorrect === true : 일치, isCorrect === false : 불일치
                if (err) {
                    resultJson = errorConfig.NOT_SIGN_UP;
                    res.status(200).send(resultJson);
                    callback(err, connection, "Bcrypt Error : ");
                }

                if (!isCorrect) {
                    resultJson = "NOT_SIGN_UP";
                } else {
                    resultJson.message = "SUCCESS";
                    resultJson.user = {
                        idx: rows[0].idx,
                        id: rows[0].id,
                        nickname: rows[0].nickname,
                        phone: rows[0].phone,
                        birth: rows[0].birth
                    };
                    resultJson.token = jwtModule.makeToken(rows[0]);
                }
                res.status(200).send(resultJson);
                callback(null, connection, "api : /login/");
            });
        }
    }

    var task = [globalModule.connect.bind(this), checkValid, selectUserInfo, onSelectComlete, globalModule.releaseConnection.bind(this)];

    async.waterfall(task, function (err, connection, result) {
        if (connection) {
            connection.release();
        }

        if (!!err) {
            console.log(result, err.message);
            if (err !== "ALREADY_SEND_MESSAGE") {
                resultJson.message = "FAILURE";
                res.status(503).send(resultJson);
            }
        } else {
            console.log(result);
        }
    });
});

/**
 * api 목적        : 회원가입 (일반)
 * request params : {
 *                      string id: "아이디", 
 *                      string password1: "비밀번호",
 *                      string password2: "비밀번호확인",
 *                      number phone: "01040908370",
 *                      string nickname: "이름",
 *                      string birth: "19950825"
 *                  }
 */
router.post('/join', function (req, res) {
    let resultJson = {
        message: '',
        code:""
    };

    let checkValid = function (connection, callback) {
        let phoneRegExp = /^(?:(010\d{4})|(01[1|6|7|8|9]\d{3,4}))(\d{4})$/;
        let emailRegExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
        let result = globalModule.checkBasicValid(req.body);

        if (result !== "OK") {
            resultJson.code = result.code;
            resultJson.message = result.message;
            res.status(200).send(resultJson);
            callback("ALREADY_SEND_MESSAGE", connection, "api : /login/join");
        } else if (req.body.password1 !== req.body.password2) {
            resultJson.code = errorConfig.NOT_CORRESPOND.code;
            resultJson.message = errorConfig.NOT_CORRESPOND.message;
            res.status(200).send(resultJson);
            callback("ALREADY_SEND_MESSAGE", connection, "api : /login/join");
        } else if (!phoneRegExp.test(req.body.phone) || !emailRegExp.test(req.body.id)) {
            resultJson.code = errorConfig.NOT_MATCH_REGULATION.code;
            resultJson.message = errorConfig.NOT_MATCH_REGULATION.message;
            res.status(200).send(resultJson);
            callback("ALREADY_SEND_MESSAGE", connection, "api : /login/join");
        } else {
            callback(null, connection);
        }
    }

    let checkAlreadyJoin = function (connection, callback) {
        connection.query('select * from User where id = ?', req.body.id, function (error, rows) {
            if (error) {
                callback(error, connection, "Select query Error : ");
            } else {
                if (rows.length !== 0) {
                    resultJson.code = errorConfig.Already_join.code;
                    resultJson.message = errorConfig.Already_join.message;
                    res.status(200).send(resultJson);
                    callback("ALREADY_SEND_MESSAGE", connection, "api : /login/join");
                } else {
                    callback(null, connection);
                }
            }
        });
    }

    var bcryptedPassword = function (connection, callback) {
        console.log('bcryptedPassword', callback);
        bcrypt.hash(req.body.password1, null, null, function (err, hash) {
            if (err) {
                callback(err, connection, "Bcrypt hashing Error : ");
            } else {
                console.log(hash);
                callback(null, connection, hash);
            }
        });
    }
    //4. DB에 저장
    var insertUserInfo = function (connection, hash, callback) {
        console.log('insertUserInfo');
        let insertQuery =
            "insert into User" +
            "(id, password, nickname, phone, birth )" +
            "values (?,?,?,?,?)";
        let params = [
            req.body.id,
            hash,
            req.body.nickname,
            req.body.phone,
            Date(req.body.birth)
        ];
        connection.query(insertQuery, params, function (err, data) {
            if (err) {
                callback(err, connection, "insert query error : ");
            }
            else {
                resultJson.message = 'SUCCESS';
                res.status(200).send(resultJson);
                callback(null, connection, "api : /login/join");
            }
        });
    }

    var task = [globalModule.connect.bind(this), checkValid, checkAlreadyJoin, bcryptedPassword, insertUserInfo, globalModule.releaseConnection.bind(this)];

    async.waterfall(task, function (err, connection, result) {
        if (connection) {
            connection.release();
        }

        if (!!err) {
            console.log("err", err, result, err.message);
            if (err !== "ALREADY_SEND_MESSAGE") {
                resultJson.message = "FAILURE";
                res.status(503).send(resultJson);
            }
        } else {
            console.log(result);
        }
    });
});

/**
 * api 목적        : 로그인 (sns)
 * request params : {string id: "아이디", string spassword: "비밀번호"}
 */
router.post('/sns', function (req, res) {
    
});


/**
 * api 목적        : 아이디 찾기
 * request params : {string name: "이름", int phone: "핸드폰번호"}
 */
router.post('/find_id', function (req, res) {
    var resultJson = {
        id: '',
        message: ''
    };

    var onSelectId = function (connection, rows, callback) {
        if (rows.length === 0) {
            // 해당 회원이 없는 경우
            resultJson.message = "NO_USER";
            res.status(201).send(resultJson);
        } else {
            // 해당 회원이 있는 경우
            var id = rows[0].id.split("@");
            var length = id[0].length;

            // 아이디 변조해서 보내기
            if (length > 10) {
                id[0] = "**" + id[0].substring(2, 4) + "**" + id[0].substring(6, 8) + "*" + id[0].substring(9);
            } else if (length > 6) {
                id[0] = "*" + id[0].substring(1, 2) + "*" + id[0].substring(3, 4) + "*" + id[0].substring(5);
            } else if (length > 3) {
                id[0] = "***" + id[0].substring(3);
            } else if (length > 1) {
                id[0] = "*" + id[0].substring(1);
            }

            resultJson.message = "EXIST_MEMBER";
            resultJson.id = id[0] + +"@" + id[1];
            res.status(201).send(resultJson);
        }
        callback(null, connection, "api : find_id");
    }

    var selectId = function (connection, callback) {
        let sql = "select * from user where name = ? and phone = ?";
        let param = [req.body.name, req.body.phone];

        connection.query(sql, param, function (err, rows) {
            if (err) {
                callback(err, connection, "Select query Error : ");
            } else {
                callback(null, connection, rows);
            }
        });
    }

    var task = [globalModule.connect.bind(this), selectId, onSelectId, globalModule.releaseConnection.bind(this)];

    async.waterfall(task, function (err, connection, result) {
        if (connection) {
            connection.release();
        }

        if (!!err) {
            console.log(result, err.message);
            resultJson.message = "FAILURE";
            res.status(503).send(resultJson);
        } else {
            console.log(result);
        }
    });
});

/**
 * api 목적        : 비밀번호 찾기
 * request params : {string id: "아이디", string name: "이름", phone: "핸드폰번호"}
 */
router.post('/find_password', function (req, res) {
    var resultJson = {
        message: ''
    };

    var makeNewPassword = function () {
        var i = 10,
            text = "";
        var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        while (i-- > 0) {
            text += possible.charAt(Math.floor(Math.random() * possible.length));
        }
        return text;
    }

    var saveNewPassword = function (connection, callback) {
        let newPassword = makeNewPassword();

        bcrypt.hash(newPassword, null, null, function (err, hash) {
            if (err) {
                callback(err, connection, "Bcrypt hashing Error : ");
            } else {
                let sql = 'update user set password = ? where id = ?';
                let params = [hash, req.body.id];
                connection.query(sql, params, function (error, rows) {
                    if (error) {
                        callback(error, connection, "Selecet query Error : ");
                    } else {
                        callback(null, connection, newPassword);
                    }
                });
            }
        });
    }

    var sendMail = function (connection, newPassword, callback) {
        let transport = nodemailer.createTransport({
            service: "Gmail",
            secure: true,
            auth: {
                user: mailConfig.hyeona.user,
                pass: mailConfig.hyeona.pass
            }
        });

        let mailOption = {
            to: req.body.id,
            subject: "안녕하세요. safe, save! 입니다.",
            html: "안녕하세요,<br> 고객님의 임시 비밀번호는 " + newPassword + "입니다. <br>" +
            "<br>어플로 돌아가셔서 로그인 후 비밀번호를 변경해주세요.</br>" +
            "<br>감사합니다.</br>"
        };

        transport.sendMail(mailOption, function (error, info) {
            if (error) {
                callback(error, connection, "Transport Error : ");
            } else {
                resultJson.message = "SUCCESS";
                res.status(201).send(resultJson);
                callback(null, connection, "api : find_password");
            }
        });
    }

    var onSelectUserInfo = function (connection, rows, callback) {
        if (rows.length === 0) {
            // 존재하는 정보가 없는 경우
            resultJson.message = "NO_INFO";
            res.status(200).send(resultJson);
            callback(null, connection, "api : find_password");
        } else {
            callback(null, connection);
        }
    }

    var selectUserInfo = function (connection, callback) {
        let sql = 'select * from user where id = ? and name = ? and phone = ?';
        let params = [req.body.id, req.body.name, req.body.phone];
        connection.query(sql, params, function (error, rows) {
            if (error) {
                callback(error, connection, "Selecet query Error : ");
            } else {
                callback(null, connection, rows);
            }
        });
    }

    var task = [globalModule.connect.bind(this), selectUserInfo, onSelectUserInfo, saveNewPassword, sendMail, globalModule.releaseConnection.bind(this)];

    async.waterfall(task, function (err, connection, result) {
        if (connection) {
            connection.release();
        }

        if (!!err) {
            console.log(result, err.message);
            resultJson.message = "FAILURE";
            res.status(503).send(resultJson);
        } else {
            console.log(result);
        }
    });
});
module.exports = router;