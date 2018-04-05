const errorConfig =  require('../config/error');
const pool = require('../config/dbPool');

var connect = function (callback) {
    pool.getConnection(function (err, connection) {
        if (err) {
            console.log("get Connection error : ", err.message);
            callback(err, connection, null);
        } else callback(null, connection);
    });
}

var releaseConnection = function (connection, apiName, callback) {
    connection.release();
    callback(null, null, apiName);
};

var checkBasicValid = function (params) {
    for (let param in params) {
        if (params[param] == null) return errorConfig.EMPTY_VALUE;
        else if (params[param].trim() === "") return errorConfig.NULL_VALUE;
    }
    return "OK";
}

module.exports.connect = connect;
module.exports.releaseConnection = releaseConnection;
module.exports.checkBasicValid = checkBasicValid;