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

router.post('/store_data', function (req, res) {
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
router.get('/', function (req, res) {
    var resultModelJson = {
        message: 'SUCCESS',
        landmarkList: []
    }
    let selectLandmark = function (connection, callback) {
      var decoded = jwtModule.decodeToken(req.headers.token);
        let selectQuery =
            "select Landmark.idx,Landmark.name, Landmark.content, Landmark.category, "+
            "Landmark.lat ,Landmark.lng, Tour.landmark_idx as isVisit from Landmark "+  
            "left join Tour on  Landmark.idx = Tour.landmark_idx and Tour.user_idx= ? "
        connection.query(selectQuery, decoded.token.idx  ,function (err, data) {
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
                        if(data[x].isVisit == null ||data[x].isVisit =="" ){
                            landmark.isVisit =0;

                        }else{
                            landmark.isVisit =1;
                                      
                    }
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
 * api 목적 : 전제 landmark 조회 (이전 코드 나중에 안드로이드 고쳐지면 지우기)
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
        connection.query(selectQuery,function (err, data) {
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

module.exports = router;

