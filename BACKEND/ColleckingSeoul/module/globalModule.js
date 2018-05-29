const errorConfig =  require('../config/error');
const pool = require('../config/dbPool');

let connect = function (callback) {
    pool.getConnection(function (err, connection) {
        if (err) {
            console.log("get Connection error : ", err.message);
            callback(err, connection, null);
        } else callback(null, connection);
    });
}

let releaseConnection = function (connection, apiName, callback) {
    connection.release();
    callback(null, null, apiName);
};

let checkBasicValid = function (params) {
    let phoneRegExp = /^(?:(010\d{4})|(01[1|6|7|8|9]\d{3,4}))(\d{4})$/;
    let emailRegExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
    let birthRegExp = /^(19[7-9][0-9]|20\d{2})-(0[0-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$/;
    for (let param in params) {
        if ((param === "id" && !emailRegExp.test(params[param])) 
            || param === ("phone" && !phoneRegExp.test(params[param])
            || param === "birth" && !birthRegExp.test(params[param]))) 
            return errorConfig.NOT_MATCH_REGULATION;

        if (params[param] == null) return errorConfig.EMPTY_VALUE;
        else if ((params[param] + "").trim() === "") return errorConfig.NULL_VALUE;
    }
    return "OK";
};

let asyncCallback = function (err, connection, result, res) {
    if (connection) connection.release();

    if (!!err && err !== "ALREADY_SEND_MESSAGE") {
        console.log("err", err, result, err.message);
        res.status(503).send({message: "FAILURE"});
    }
};

module.exports.connect = connect;
module.exports.releaseConnection = releaseConnection;
module.exports.checkBasicValid = checkBasicValid;
module.exports.asyncCallback = asyncCallback;