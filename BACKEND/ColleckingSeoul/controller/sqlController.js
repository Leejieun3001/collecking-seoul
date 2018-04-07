const errorConfig =  require('../config/error');

let selectUserById = function (connection, id) {
    connection.query('select * from User where id = ?', id, function (error, rows) {
        if (error) return {error: error, rows: null}; 

        return {error: null, rows: rows};
    });
}

module.exports.selectUserById = selectUserById;