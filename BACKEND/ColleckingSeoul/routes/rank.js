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
 * api 목적 : 사용자 랭킹 조회
 * requert params : 없음
 */

router.get('/user_rank', function (req, res) {
    var resultModelJson = {
        message: 'SUCCESS',
        userRankList: []
    }
    let selectUserRank = function (connection, callback) {
        let selectQuery =
              'SELECT  User.nickname, Photo.url FROM Tour, User, Photo '+
              'where Photo.user_idx = User.idx '+
              'and Tour.user_idx = User.idx '+ 
              'group by Tour.user_idx '+
              'order by count(*) DESC'
        connection.query(selectQuery, function (err, data) {
            if (err) {
                callback(err, connection, "select query error : ", res);
            }
            else {
                if (data.length !== 0) {
                    for (var x in data) {
                        var userRank = {}
                        userRank.nickname = data[x].nickname;
                        userRank.url = data[x].url;
                        resultModelJson.userRankList.push(userRank);
                    }
                }
                res.status(200).send(resultModelJson);
                callback(null, connection, "api : /rank/user_rank");
            }
        });
    }
    var task = [globalModule.connect.bind(this), selectUserRank, globalModule.releaseConnection.bind(this)];
    async.waterfall(task, globalModule.asyncCallback.bind(this));

});

/**
 * api 목적 : 랜드마크 순위 조회
 * requset params : 없음
 */

router.get('/landmark_rank', function (req, res) {
    var resultModelJson = {
        message: 'SUCCESS',
        landmarkRankList: []
    }
    let selectLandmarkRank = function (connection, callback) {
        let selectQuery =
          'SELECT  Landmark.name FROM Landmark, Tour '+ 
          'where Tour.landmark_idx = Landmark.idx '+
          'group by landmark_idx '+
          'order by count(*) DESC'
        connection.query(selectQuery, function (err, data) {
            if (err) {
                callback(err, connection, "select query error : ", res);
            }
            else {
                if (data.length !== 0) {
                    for (var x in data) {
                        var landmarkRank = {}              
                        landmarkRank.name = data[x].name;                     
                        resultModelJson.landmarkRankList.push(landmarkRank);
                    }
                }
                res.status(200).send(resultModelJson);
                callback(null, connection, "api : /rank/landmark_rank");
            }
        });
    }
    var task = [globalModule.connect.bind(this), selectLandmarkRank, globalModule.releaseConnection.bind(this)];
    async.waterfall(task, globalModule.asyncCallback.bind(this));

});

module.exports = router;


