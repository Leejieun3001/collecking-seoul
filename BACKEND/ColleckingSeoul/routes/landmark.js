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
/*
var LandmarkTraditionName = [
    "TraditionalCulture",
    "한국문화의집 코우스(KOUS)",
    "쌈지길 체험공방",
    "서울김치문화체험관",
    "청원산방",
    "이종남 자연염색연구소",
    "서울풍물시장 전통문화체험관",
    "티 게스트하우스",
    "결련택견 중앙본부 전수관",
    "북촌한옥마을",
    "안동교회 소허당",
    "궁중음식연구원",
    "서울놀이마당",
    "아름지기",
    "민속극장 풍류",
    "하늘물빛(전통 천연 염색 연구소)",
    "남산골 한옥마을",
    "리기태전통연공방",
    "삼청각",
    "서울 무형문화재 교육전시장",
    "금현국악원",
    "한상수자수박물관",
    "한국의집",
    "옻칠공방",
    "북촌문화센터",
    "국기원",
    "예지원",
    "봉산재 (북촌아트센터)",
    "한국 옛 인형일기 소연(素鉛)",
    "서울남산국악당",
    "락고재"
];

router.post('/', function (req, res) {
    var lat;
    var lng;
    var options = {
        provider: 'google',
        httpAdapter: 'https',
        apiKey: 'AIzaSyBLBe_B6d5VjhFaUMEhtFc20SqrPrXlTW4',
        formatter: null
    };
    var geocoder;
    geocoder = NodeGeocoder(options);
    console.log('for문 전');

    let insertData = function (connection, callback) {

        for (var i = 0; i < LandmarkTraditionName.length; i++) {
            var faddr = LandmarkTraditionName[i];
            console.log(faddr);
            let address = faddr;
            geocoder.geocode(address, function (err, res) {
                console.log(address);
                if (err) {
                    console.log('err geocode')
                } else {
                   // console.log(res[0].latitude);
                    //console.log(res[0].longitude);
                    try {
                        let insertQuery =
                            "insert into Landmark" +
                            "(name, lat, lng, category)" +
                            "values (?,?,?,?)";
                        let params = [
                            address,
                            res[0].latitude,
                            res[0].longitude,
                            'TraditionalCulture'
                        ];
                        connection.query(insertQuery, params, function (err, data) {
                            if (err) {
                                callback(err, connection, "insert query error : ", res);
                            }
                            else {
                                callback(null, connection, "api : /landmark");
                            }
                        });
                    } catch (e) {
                        console.log(e.message);
                    }

                }
            });
        }
    }
    var task = [globalModule.connect.bind(this), insertData, globalModule.releaseConnection.bind(this)];
    async.waterfall(task, globalModule.asyncCallback.bind(this));
});

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

