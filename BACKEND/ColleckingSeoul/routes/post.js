const pool = require('../config/dbPool');
const router = require('express').Router();
const bcrypt = require('bcrypt-nodejs');
const async = require('async');
const globalModule = require('../module/globalModule');
const moment = require('moment');
const errorConfig = require('../config/error');
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
        key: function (req, file, cb) {
            cb(null, Date.now() + '.' + file.originalname.split('.').pop())
        }
    })
});


/**
 * api 목적        : 글등록 
 * request params : {string title: "글제목", 
 *                   string content: "내용",
 *                   int user_idx :"회원 인덱스",
 *                   int landmark_idx : "랜드마크 인덱스",
 *                   File photo : "글 사진" }
 */
router.post('/write_post', upload.single('photo'), function (req, res) {
    let resultJson = {
        message: '',
        code: "",
        user: null
    };
    var boardIndex;


    let insertPost = function (connection, callback) {
        let insertQuery =
            "insert into Board" +
            "(title, content, date, user_idx, landmark_idx)" +
            "values (?,?,?,?,?)";
        let params = [
            req.body.title,
            req.body.content,
            moment(new Date()).format('YYYY-MM-DD HH:MM:SS'),
            req.body.user_idx,
            req.body.landmark_idx
        ];
        connection.query(insertQuery, params, function (err, data) {
            if (err) callback(err, connection, "insert query error : ", res);
            else callback(null, connection);
        });
    }

    let selectPostId = function (connection, callback) {
        let selectQuery =
            "select idx from Board where user_idx= ? and landmark_idx = ? ";
        let params = [
            req.body.user_idx,
            req.body.landmark_idx
        ];
        connection.query(selectQuery, params, function (err, data) {
            if (err) callback(err, connection, "insert query error : ", res);
            else {
                boardIndex = data[0].idx;
                callback(null, connection);
            }
        });

    }
    let insertPhoto = function (connection, callback) {
        let insertQuery =
            "insert into Photo" +
            "(user_idx , board_idx, landmark_idx , url)" +
            "values (?,?,?,?)";
        let params = [
            req.body.user_idx,
            boardIndex,
            req.body.landmark_idx,
            req.file.location
        ];

        connection.query(insertQuery, params, function (err, data) {
            if (err) {
                callback(err, connection, "insert query error : ", res);
            }
            else {
                res.status(200).send({ message: "SUCCESS" });
                callback(null, connection, "api : /post/write_post");
            }
        });
    }
    var task = [globalModule.connect.bind(this), insertPost, selectPostId, insertPhoto, globalModule.releaseConnection.bind(this)];
    async.waterfall(task, globalModule.asyncCallback.bind(this));
});

module.exports = router;