const pool = require('../config/dbPool');
const router = require('express').Router();
const bcrypt = require('bcrypt-nodejs');
const async = require('async');
const globalModule = require('../module/globalModule');
const sqlController = require('../controller/sqlController');
const nodemailer = require('nodemailer');
const mailConfig = require('../config/mailAccount');
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

/**
 * api 목적        : 회원가입 (일반)
 * request params : {
 *                      string id: "아이디", 
 *                      string password1: "비밀번호",
 *                      string password2: "비밀번호확인",
 *                      string phone: "01040908370",
 *                      string nickname: "이름",
 *                      string birth: "19950825",
 *                      number sex: 0(남자) || 1(여자)
 *                      File photo
 *                  }
 */
router.post('/', upload.single('photo'), function (req, res) {
    let checkValid = function (connection, callback) {
        let result = globalModule.checkBasicValid(req.body);

        if (result !== "OK") {
            res.status(200).send(result);
            callback("ALREADY_SEND_MESSAGE", connection, "api : /login/join");
        } else if (req.body.password1 !== req.body.password2) {
            res.status(200).send(errorConfig.NOT_CORRESPOND);
            callback("ALREADY_SEND_MESSAGE", connection, "api : /login/join");
        } else {
            callback(null, connection);
        }
    }

    let checkAlreadyJoin = function (connection, callback) {
        connection.query('select * from User where id = ?', req.body.id, function (error, rows) {
            if (error) {
                callback(error, connection, "Select query Error : ", res);
            } else {
                if (rows.length !== 0) {
                    res.status(200).send(errorConfig.ALREADY_JOIN);
                    callback("ALREADY_SEND_MESSAGE", connection, "api : /login/join");
                } else {
                    callback(null, connection);
                }
            }
        });
    }

    let bcryptedPassword = function (connection, callback) {
        bcrypt.hash(req.body.password1, null, null, function (err, hash) {
            if (err) callback(err, connection, "Bcrypt hashing Error : ",res);
            else callback(null, connection, hash);
        });
    }
    //4. DB에 저장
    let insertUserInfo = function (connection, hash, callback) {
        let insertQuery =
            "insert into User" +
            "(id, password, nickname, phone, birth, sex )" +
            "values (?,?,?,?,?)";
        let params = [
            req.body.id,
            hash,
            req.body.nickname,
            req.body.phone,
            Date(req.body.birth),
            req.body.sex
        ];
        connection.query(insertQuery, params, function (err, data) {
            if (err) callback(err, connection, "insert query error : ", res);
            else callback(null, connection);
        });
    }

    let insertPhoto = function (connection, callback) {
        let insertQuery =
            "insert into Photo" +
            "(user_idx, url)" +
            "values ((select idx from User where id = ?),?)";
        let imageUrl = !req.file ? require('../config/secretKey').default_male : req.file.location;
        let params = [
            req.body.id,
            imageUrl
        ];
        connection.query(insertQuery, params, function (err, data) {
            if (err) {
                callback(err, connection, "insert query error : ", res);
            }
            else {
                res.status(200).send({message: "SUCCESS"});
                callback(null, connection, "api : /login/join");
            }
        });
    }

    var task = [globalModule.connect.bind(this), checkValid, checkAlreadyJoin, bcryptedPassword, insertUserInfo, insertPhoto, globalModule.releaseConnection.bind(this)];
    async.waterfall(task, globalModule.asyncCallback.bind(this));
});

/**
 * api 목적        : 아이디 중복체크
 * request params : { string id: "아이디" }
 */
router.get('/check_dupplicate', function (req, res) {
    let checkValid = function (connection, callback) {
        let result = globalModule.checkBasicValid(req.query);

        if (result !== "OK") {
            res.status(200).send(result);
            callback("ALREADY_SEND_MESSAGE", connection, "api : /join/check_dupplicate");
        } else {
            callback(null, connection);
        }
    }

    let checkID = function (connection, callback) {
        connection.query('select * from User where id = ?', req.query.id, function (error, rows) {
            if (error) {
                callback(error, connection, "Select query Error : ", res);
            } else {
                if (rows.length !== 0) {
                    res.status(200).send(errorConfig.ALREADY_JOIN);
                    callback("ALREADY_SEND_MESSAGE", connection, "api : /join/check_dupplicate");
                } else {
                    res.status(200).send({message: "SUCCESS"});
                    callback(null, connection);
                }
            }
        });
    }

    var task = [globalModule.connect.bind(this), checkValid, checkID, globalModule.releaseConnection.bind(this)];
    async.waterfall(task, globalModule.asyncCallback.bind(this));
});

/**
 * api 목적        : email 인증
 * request params : tempEmail(query)
 */

router.get('/verificationCode', function (req, res) {

    var Transport = nodemailer.createTransport({
        service: "Gmail",
        auth: {
            user: mailConfig.jieun.user,
            pass: mailConfig.jieun.pass
        }
    });

    var rand;
    var resultJson = {
        message: '',
        verificationCode: ''
    };

    var connect = function (callback) {
        pool.getConnection(function (err, connection) {
            if (err) {
                console.log("getConnection error : ", err);
                callback(err, connection, null);
            }
            else callback(null, connection);
        });
    }

    var selectUserInfo = function (connection, callback) {
        let duplicate_check_query = "select * from User where id = ?";
        connection.query(duplicate_check_query, req.query.tempEmail, function (err, data) {
            if (err) {
                console.log("duplicate check select query error : ", err);
                callback(err, connection, null);
            } else {
                if (data.length == 0) {
                    callback(null, connection);
                }
                else {
                    resultJson.message = "duplicated";
                    resultJson.detail = "unable to sign up";
                    res.status(201).send(resultJson);
                    callback('ok', connection);
                }
            }
        });
    }

    var sendMail = function (connection, callback) {
        rand = Math.floor((Math.random() * 10000));
        let mailOption = {
            to: req.query.tempEmail,
            subject: "안녕하세요. CollecKing Seoul! 입니다.",
            html: "안녕하세요,<br> 고객님의 인증번호는 " + rand + "입니다. <br>"
            + "<br>어플로 돌아가셔서 인증번호를 입력해 주세요.</br>"
            + "<br>감사합니다.</br>"
        };
        Transport.sendMail(mailOption, function (error, info) {
            if (error) {
                return console.log(error);
            } else {
                resultJson.message = 'email success';
                resultJson.verificationCode = rand;
                res.status(201).send(resultJson);
                callback(null, connection);

            }
        });
    }

    var releaseConnection = function (connection, callback) {
        connection.release();
        callback(null, null, '-verificationCode');
    }

    var verificationCode_task = [globalModule.connect.bind(this), selectUserInfo, sendMail, globalModule.releaseConnection.bind(this)];
    async.waterfall(task, globalModule.asyncCallback.bind(this));
});
module.exports = router;