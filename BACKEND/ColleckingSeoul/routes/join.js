const pool = require('../config/dbPool');
const router = require('express').Router();
const bcrypt = require('bcrypt-nodejs');
const async = require('async');
const jwtModule = require('../module/jwtModule');
const globalModule = require('../module/globalModule');
const sqlController = require('../controller/sqlController');
const nodemailer = require('nodemailer');
const mailConfig = require('../config/mailAccount');
const errorConfig =  require('../config/error');

/**
 * api 목적        : 회원가입 (일반)
 * request params : {
 *                      string id: "아이디", 
 *                      string password1: "비밀번호",
 *                      string password2: "비밀번호확인",
 *                      string phone: "01040908370",
 *                      string nickname: "이름",
 *                      string birth: "19950825"
 *                  }
 */
router.post('/', function (req, res) {
    let checkValid = function (connection, callback) {
        let phoneRegExp = /^(?:(010\d{4})|(01[1|6|7|8|9]\d{3,4}))(\d{4})$/;
        let emailRegExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
        let result = globalModule.checkBasicValid(req.body);

        if (result !== "OK") {
            res.status(200).send(result);
            callback("ALREADY_SEND_MESSAGE", connection, "api : /login/join");
        } else if (req.body.password1 !== req.body.password2) {
            res.status(200).send(errorConfig.NOT_CORRESPOND);
            callback("ALREADY_SEND_MESSAGE", connection, "api : /login/join");
        } else if (!phoneRegExp.test(req.body.phone) || !emailRegExp.test(req.body.id)) {
            res.status(200).send(errorConfig.NOT_MATCH_REGULATION);
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

    var bcryptedPassword = function (connection, callback) {
        bcrypt.hash(req.body.password1, null, null, function (err, hash) {
            if (err) {
                callback(err, connection, "Bcrypt hashing Error : ",res);
            } else {
                console.log(hash);
                callback(null, connection, hash);
            }
        });
    }
    //4. DB에 저장
    var insertUserInfo = function (connection, hash, callback) {
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
                callback(err, connection, "insert query error : ", res);
            }
            else {
                res.status(200).send({message: "SUCCESS"});
                callback(null, connection, "api : /login/join");
            }
        });
    }

    var task = [globalModule.connect.bind(this), checkValid, checkAlreadyJoin, bcryptedPassword, insertUserInfo, globalModule.releaseConnection.bind(this)];
    async.waterfall(task, globalModule.asyncCallback.bind(this));
});

/**
 * api 목적        : 아이디 중복체크
 * request params : { string id: "아이디" }
 */
router.get('/check_dupplicate', function (req, res) {
    let checkValid = function (connection, callback) {
        let emailRegExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
        let result = globalModule.checkBasicValid(req.query);

        if (result !== "OK") {
            res.status(200).send(result);
            callback("ALREADY_SEND_MESSAGE", connection, "api : /join/check_dupplicate");
        } else if (!emailRegExp.test(req.query.id)) {
            res.status(200).send(errorConfig.NOT_MATCH_REGULATION);
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

module.exports = router;