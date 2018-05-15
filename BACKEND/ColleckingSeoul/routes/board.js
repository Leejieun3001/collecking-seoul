const pool = require('../config/dbPool');
const router = require('express').Router();
const async = require('async');
const jwtModule = require('../module/jwtModule');
const globalModule = require('../module/globalModule');
const mailConfig = require('../config/mailAccount');
const errorConfig =  require('../config/error');
const moment = require('moment');
const aws = require('aws-sdk');
const multer = require('multer');
const multerS3 = require('multer-s3');
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
 * api 목적        : 상세글 조회
 * request params : {string idx: "글 아이디"}
 */
router.get('/', function (req, res) {
    let resultJson = {
        message: '',
        isMine: 0,
        board: null
    };

    let checkToken = function (connection, callback) {
        var decodedToken = jwtModule.decodeToken(req.headers.token);
        if (!decodedToken.hasOwnProperty('token')) {
            res.status(200).send(decodedToken);
            callback("ALREADY_SEND_MESSAGE", connection, "api : /board/");
        } else {
            callback(null, decodedToken.idx, connection);
        }
    };

    let checkValid = function (connection, u_idx, callback) {
        if (req.query.idx === "" || req.query.idx == null) {
            res.status(200).send(errorConfig.NULL_VALUE);
            callback("ALREADY_SEND_MESSAGE", connection, "api : /board/");
        } else {
            callback(null, u_idx, connection);
        }
    }

    let selectBoard = function (connection, u_idx, callback) {
        connection.query('select b.b_idx bIdx, title, b_content content, u_idx uIdx, nickname, date, p.url from BoardListView b '
            + 'left join Photo p on b.b_idx = p.board_idx '
            + 'where b.b_idx = ? ', 
            req.query.idx, function (error, rows) {
            if (error) callback(error, connection, "Selecet query Error : ");
            else {
                if (rows.length === 0) {
                    res.status(200).send(errorConfig.NO_DATA);
                    callback("ALREADY_SEND_MESSAGE", connection, "api : /board/");
                } else {
                    resultJson.message = "SUCCESS";
                    resultJson.board = rows[0];
                    if (rows[0].u_idx === u_idx) { resultJson.isMine = 1; }
                    else { resultJson.isMine = 0; }
                    res.status(200).send(resultJson);
                    callback(null, connection);
                }
            }
        });
    }

    var task = [globalModule.connect.bind(this), checkToken, checkValid, selectBoard, globalModule.releaseConnection.bind(this)];
    async.waterfall(task, globalModule.asyncCallback.bind(this));
});

/**
 * api 목적        : 전체글 조회
 * request params : {string idx: "랜드마크 아이디"}
 */
router.get('/total', function (req, res) {
    let resultJson = {
        message: '',
        boards: null
    };

    let checkToken = function (connection, callback) {
        var decodedToken = jwtModule.decodeToken(req.headers.token);
        if (!decodedToken.hasOwnProperty('token')) {
            res.status(200).send(decodedToken);
            callback("ALREADY_SEND_MESSAGE", connection, "api : /board/total");
        } else {
            callback(null, connection);
        }
    };

    let checkValid = function (connection, callback) {
        if (req.query.idx === "" || req.query.idx == null) {
            res.status(200).send(errorConfig.NULL_VALUE);
            callback("ALREADY_SEND_MESSAGE", connection, "api : /board/total");
        } else {
            callback(null, connection);
        }
    }

    let selectBoardList = function (connection, callback) {
        connection.query('select b.b_idx idx, title, b_content content, nickname, date, p.url from BoardListView b '
            + 'left join Photo p on b.b_idx = p.board_idx '
            + 'where b.l_idx = ?', 
            req.query.idx, function (error, rows) {
            if (error) callback(error, connection, "Selecet query Error : ");
            else {
                resultJson.message = "SUCCESS";
                resultJson.boards = rows[0];
                res.status(200).send(resultJson);
                callback(null, connection, "api : /board/total");
            }
        });
    }

    var task = [globalModule.connect.bind(this), checkToken, checkValid, selectBoardList, globalModule.releaseConnection.bind(this)];
    async.waterfall(task, globalModule.asyncCallback.bind(this));
});

/**
 * api 목적        : 글등록 
 * request params : {string title: "글제목", 
 *                   string content: "내용",
 *                   int user_idx :"회원 인덱스",
 *                   int landmark_idx : "랜드마크 인덱스",
 *                   File photo : "글 사진" }
 */
router.post('/write', upload.single('photo'), function (req, res) {
    let resultJson = {
        message: '',
        code: "",
        user: null
    };
    var boardIndex;

    let insertBoard = function (connection, callback) {
      var decodedToken = jwtModule.decodeToken(req.headers.token).token;
        let insertQuery =
            "insert into Board" +
            "(title, content, user_idx, landmark_idx)" +
            "values (?,?,?,?)";
        let params = [
            req.body.title,
            req.body.content,
            decodedToken.idx,
            req.body.landmark_idx
        ];
        connection.query(insertQuery, params, function (err, data) {
            if (err) callback(err, connection, "insert query error : ", res);
            else callback(null, connection, decodedToken.idx);
        });
    }

    let selectPostId = function (connection, userIdx, callback) {
        let selectQuery =
            "select idx from Board where user_idx = ? and landmark_idx = ? ";
        let params = [
            userIdx,
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
        var decodedToken = jwtModule.decodeToken(req.headers.token).token;
        let insertQuery =
            "insert into Photo" +
            "(user_idx , board_idx, landmark_idx , url)" +
            "values (?,?,?,?)";
        let params = [
            decodedToken.idx,
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
    var task = [globalModule.connect.bind(this), insertBoard, selectPostId, insertPhoto, globalModule.releaseConnection.bind(this)];
    async.waterfall(task, globalModule.asyncCallback.bind(this));
});

/** 
 * api 목적 :  서버 글 수정
 * * request params : {
 *                   int idx : "개시글 번호",
 *                   string title: "글제목", 
 *                   string content: "내용",
 *                   int user_idx :"회원 인덱스",
 *                   File photo : "글 사진" }
*/

router.post('/modify', upload.single('photo'), function (req, res) {
    let resultJson = {
        message: '',
        code: "",
        user: null
    };
    var boardIndex;


    let modifyBoard = function (connection, callback) {
        let modifyQuery =
            "update Board " +
            "set title =?, content =?, date =?" +
            "where idx =?";
        let params = [
            req.body.title,
            req.body.content,
            moment(new Date()).format('YYYY-MM-DD HH:MM:SS'),
            req.body.idx
        ];
        connection.query(modifyQuery, params, function (err, data) {
            if (err) callback(err, connection, "update query error : ", res);
            else callback(null, connection);
        });
    }


    let modifyPhoto = function (connection, callback) {

        let updateQuery =
            "update Photo " +
            "set url =? "+
            "where board_idx =?";
        let params = [
            req.file.location,
            req.body.board_idx
        ];

        connection.query(updateQuery, params, function (err, data) {
            if (err) {
                callback(err, connection, "update query error : ", res);
            }
            else {
                res.status(200).send({ message: "SUCCESS" });
                callback(null, connection, "api : /post/modify_post");
            }
        });
    }
    var task = [globalModule.connect.bind(this), modifyBoard, modifyPhoto, globalModule.releaseConnection.bind(this)];
    async.waterfall(task, globalModule.asyncCallback.bind(this));
});

module.exports = router;