const pool = require('../config/dbPool');
const router = require('express').Router();
const async = require('async');
const globalModule = require('../module/globalModule');
const moment = require('moment');
const errorConfig = require('../config/error');
const aws = require('aws-sdk');
const jwtModule = require('../module/jwtModule');
const NodeGeocoder = require('node-geocoder');
var parser = require('json-parser');

/**
 * api 목적 : 서버에 데이터 저장
 * requset { string :  sql (insert문)} 
 */

router.post('/', function (req, res) {
    var resultJson = {
        message: '',
        detail: ''
    };
    let insertData = function (connection, callback) {
        let insertQuery = req.query.sql;
        console.log(req.query.sql);
        connection.query(insertQuery, function (err, data) {
            if (err) {
                callback(err, connection, "insert query error : ", res);
            }
            else {
                console.log("data");
                resultJson.message = 'SUCCESS';
                res.status(200).send(resultJson);
                callback(null, connection, "api : /landmark");
            }
        });
    }
    var task = [globalModule.connect.bind(this), insertData, globalModule.releaseConnection.bind(this)];
    async.waterfall(task, globalModule.asyncCallback.bind(this));
});

/**
 * api 목적 : 전제 landmark 조회
 * requset : 없음 
 */
router.get('/landmark_list', function (req, res) {
    var resultModelJson = {
        message: 'SUCCESS',
        landmarkList: []
    }
    let selectLandmark = function (connection, callback) {
        let selectQuery =
            "select * from Landmark"
        connection.query(selectQuery, function (err, data) {
            if (err) {
                callback(err, connection, "select query error : ", res);
            }
            else {
                if (data.length !== 0) {
                    for (var x in data) {
                        var landmark = {}
                        landmark.idx = data[x].idx;
                        landmark.name = data[x].name;
                        landmark.content = data[x].content;
                        landmark.lat = data[x].lat;
                        landmark.lng = data[x].lng;
                        landmark.category = data[x].category;
                        resultModelJson.landmarkList.push(landmark);
                    }
                }
                res.status(200).send(resultModelJson);
                callback(null, connection, "api : /landmark/landmark_list");
            }
        });
    }
    var task = [globalModule.connect.bind(this), selectLandmark, globalModule.releaseConnection.bind(this)];
    async.waterfall(task, globalModule.asyncCallback.bind(this));

});

/**
 * api 목적 : 사용자 방문 landmark 데이터 조회
 * requset : token 
 */
router.get('/landmark_userlist', function (req, res) {
    var resultModelJson = {
        message: 'SUCCESS',
        landmarkList: []
    }
    let selectLandmark = function (connection, callback) {
        var decodedToken = jwtModule.decodeToken(req.headers.token);

        connection.query("select Landmark.idx, Landmark.name from Landmark " +
            "join Tour on Tour.landmark_idx = Landmark.idx " +
            "where Tour.user_idx = ?  ", decodedToken.idx, function (err, data) {
                if (err) {
                    callback(err, connection, "select query error : ", res);
                }
                else {
                    if (data.length !== 0) {
                        for (var x in data) {
                            var userlandmarkList = {}
                            userlandmarkList.idx = data[x].name;
                            userlandmarkList.name = data[x].name;
                            resultModelJson.landmarkList.push(userlandmarkList);
                        }
                    }
                    res.status(200).send(resultModelJson);
                    callback(null, connection, "api : /landmark/landmark_userlist");
                }
            });
    }
    var task = [globalModule.connect.bind(this), selectLandmark, globalModule.releaseConnection.bind(this)];
    async.waterfall(task, globalModule.asyncCallback.bind(this));
});

module.exports = router;

