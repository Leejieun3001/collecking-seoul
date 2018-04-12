const pool = require('../config/dbPool');
const router = require('express').Router();
const bcrypt = require('bcrypt-nodejs');
const async = require('async');
const jwtModule = require('../module/jwtModule');
const globalModule = require('../module/globalModule');
const nodemailer = require('nodemailer');
const mailConfig = require('../config/mailAccount');
const errorConfig =  require('../config/error');

Date.prototype.yyyymmdd = function() {
    var mm = this.getMonth() + 1; // getMonth() is zero-based
    var dd = this.getDate();
  
    return [this.getFullYear(),
            (mm>9 ? '' : '0') + mm,
            (dd>9 ? '' : '0') + dd
           ].join('');
};

/**
 * api 목적        : 로그인 (일반)
 * request params : {string id: "아이디", string password: "비밀번호"}
 */
router.post('/', function (req, res) {
    let resultJson = {
        message: '',
        code:"",
        user: null
    };

    let checkValid = function (connection, callback) {
        let result = globalModule.checkBasicValid(req.body);
        if (result !== "OK") {
            res.status(200).send(result);
            callback("ALREADY_SEND_MESSAGE", connection, "api : /login/");
        } else {
            callback(null, connection);
        }
    }

    let selectUserInfo = function (connection, callback) {
        connection.query('select * from User left outer join Photo ' + 
                        'on User.idx=Photo.user_idx ' + 
                        'where id = ?', req.body.id, function (error, rows) {
            if (error) callback(error, connection, "Selecet query Error : ");
            else {
                if (rows.length === 0) {
                    // 존재하는 아이디가 없는 경우
                    res.status(200).send(errorConfig.NOT_SIGN_UP);
                    callback("ALREADY_SEND_MESSAGE", connection, "api : /login/");
                } else {
                    if (rows[0].snsCategory !== 0) {
                        res.status(200).send(errorConfig.IS_SNS_ACCOUNT);
                        callback("ALREADY_SEND_MESSAGE", connection, "api : /login/");
                    } else {
                        callback(null, connection, rows);
                    }
                }
            }
        });
    }

    let comparePW = function (connection, rows, callback) {
        bcrypt.compare(req.body.password, rows[0].password, function (err, isCorrect) {
            // isCorrect === true : 일치, isCorrect === false : 불일치
            if (err) {
                res.status(200).send(errorConfig.NOT_SIGN_UP);
                callback(err, connection, "Bcrypt Error : ");
            }

            if (!isCorrect) {
                res.status(200).send(errorConfig.INCORRECT_PASSWORD);
            } else {
                resultJson.message = "SUCCESS";
                resultJson.user = {
                    idx: rows[0].idx,
                    id: rows[0].id,
                    nickname: rows[0].nickname,
                    phone: rows[0].phone,
                    birth: rows[0].birth,
                    url: rows[0].url
                };
                resultJson.token = jwtModule.makeToken(rows[0]);
                res.status(200).send(resultJson);
            }
            callback(null, connection, "api : /login/");
        });
    }

    var task = [globalModule.connect.bind(this), checkValid, selectUserInfo, comparePW, globalModule.releaseConnection.bind(this)];
    async.waterfall(task, globalModule.asyncCallback.bind(this));
});

/**
 * api 목적        : 로그인 (sns)
 * request params : {
 *                      string id: "아이디", 
 *                      string accessToken: "비밀번호",
 *                      string phone: 01040908370,
 *                      File profileUrl: profile,
 *                      int snsCategory: 1
 *                  }
 */
router.post('/sns', function (req, res) {
    let resultJson = {
        message: '',
        code:"",
        user: null
    };

    let checkValid = function (connection, callback) {
        let result = globalModule.checkBasicValid(req.body);
        if (result !== "OK") {
            res.status(200).send(result);
            callback("ALREADY_SEND_MESSAGE", connection, "api : /login/sns");
        } else {
            callback(null, connection);
        }
    }

    let selectUserInfo = function (connection, callback) {
        connection.query('select * from User ' + 
                    'left outer join Photo ' + 
                    'on User.idx=Photo.user_idx ' + 
                    'where id = ?', req.body.id, function (error, rows) {
            if (error) callback(error, connection, "Selecet query Error : ");
            else {
                if (rows.length === 0) joinForSns(connection, callback);
                else {
                    if (rows[0].snsCategory === req.body.snsCategory) {
                        callback(null, connection, rows);
                    } else {
                        res.status(200).send(errorConfig.NOT_MATCH_ACCOUNT);
                        callback("ALREADY_SEND_MESSAGE", connection, "api : /login/sns");
                    }
                }
            }
        });
    }

    let joinForSns = function (connection, callback) {
        let insertQuery = "insert into User" +
                        "(id, password, nickname, phone, snsCategory )" +
                        "values (?,?,?,?,?,?)";

        bcrypt.hash(req.body.accessToken, null, null, function (err, hash) {
            if (err) {
                callback(err, connection, "Bcrypt hashing Error : ",res);
            } else {
                let params = [ req.body.id, hash, req.body.nickname, req.body.phone, Date(req.body.birth), req.body.snsCategory ];
                let data = {
                    id: req.body.id,
                    password: hash,
                    nickname: req.body.nickname,
                    sex: 2,
                    phone: "00000000000",
                    birth: Date('19950825')
                }
                connection.query(insertQuery, params, function (error, rows) {
                    if (error) callback(error, connection, "Selecet query Error : ", res);
                    else {
                        insertQuery = "insert into Photo (user_idx, url) values ((select idx from User where id = ?),?)"
                        connection.query(insertQuery, [req.body.id, req.body.photo], function (error, rows) {
                            if (error) callback(error, connection, "Selecet query Error : ", res);
                            else callback(null, connection, data);
                        });
                    }
                });
            }
        });
    };

    let comparePW = function (connection, rows, callback) {
        bcrypt.compare(req.body.accessToken, rows[0].password, function (err, isCorrect) {
            // isCorrect === true : 일치, isCorrect === false : 불일치
            if (err) {
                res.status(200).send(errorConfig.NOT_SIGN_UP);
                callback(err, connection, "Bcrypt Error : ");
            }

            if (!isCorrect) {
                res.status(200).send(errorConfig.INCORRECT_PASSWORD);
            } else {
                resultJson.message = "SUCCESS";
                resultJson.user = {
                    idx: rows[0].idx,
                    id: rows[0].id,
                    nickname: rows[0].nickname,
                    phone: rows[0].phone,
                    birth: rows[0].birth,
                    sex: rows[0].sex
                };
                resultJson.token = jwtModule.makeToken(rows[0]);
                res.status(200).send(resultJson);
            }
            callback(null, connection, "api : /login/sns");
        });
    }

    var task = [globalModule.connect.bind(this), checkValid, selectUserInfo, globalModule.releaseConnection.bind(this)];
    async.waterfall(task, globalModule.asyncCallback.bind(this));
});


/**
 * api 목적        : 아이디 찾기
 * request params : {int phone: "핸드폰번호", birth: "19950825"}
 */
router.post('/find_id', function (req, res) {
    var resultJson = {
        id: '',
        message: ''
    };

    var selectId = function (connection, callback) {
        let sql = "select id, birth from User where phone = ?";
        let param = [req.body.phone];

        connection.query(sql, param, function (err, rows) {
            if (err) callback(err, connection, "Select query Error : ");
             else callback(null, connection, rows);
        });
    }

    var onSelectId = function (connection, rows, callback) {
        if (rows.length === 0 || (req.body.birth != rows[0].birth.yyyymmdd())) {
            // 해당 회원이 없는 경우
            res.status(200).send(errorConfig.NOT_SIGN_UP);
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
            resultJson.id = id[0] + "@" + id[1];
            res.status(201).send(resultJson);
        }
        callback(null, connection, "api : find_id");
    }

    

    var task = [globalModule.connect.bind(this), selectId, onSelectId, globalModule.releaseConnection.bind(this)];
    async.waterfall(task, globalModule.asyncCallback.bind(this));
});

/**
 * api 목적        : 비밀번호 찾기
 * request params : {string id: "아이디", phone: "핸드폰번호"}
 */
router.post('/find_password', function (req, res) {
    var resultJson = {
        message: ''
    };

    var onSelectUserInfo = function (connection, rows, callback) {
        if (rows.length === 0) {
            // 존재하는 정보가 없는 경우
            res.status(200).send(errorConfig.NOT_SIGN_UP);
            callback(null, connection, "api : find_password");
        } else {
            callback(null, connection);
        }
    }

    var selectUserInfo = function (connection, callback) {
        let sql = 'select * from User where id = ? and phone = ?';
        let params = [req.body.id, req.body.phone];
        connection.query(sql, params, function (error, rows) {
            if (error) callback(error, connection, "Selecet query Error1 : ");
            else callback(null, connection, rows);
        });
    }

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
                let sql = 'update User set password = ? where id = ?';
                let params = [hash, req.body.id];
                connection.query(sql, params, function (error, rows) {
                    if (error) callback(error, connection, "Selecet query Error2 : ");
                    else callback(null, connection, newPassword);
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
            subject: "안녕하세요. Collecking Seoul 입니다.",
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

    var task = [globalModule.connect.bind(this), selectUserInfo, onSelectUserInfo, saveNewPassword, sendMail, globalModule.releaseConnection.bind(this)];
    async.waterfall(task, globalModule.asyncCallback.bind(this));
});
module.exports = router;