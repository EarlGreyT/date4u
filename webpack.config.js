const path = require('path'); 
module.exports = {
    entry: './frontend/src/application.js',
    output: {
        path: path.resolve(__dirname, 'src', 'main', 'resources', 'static','js'),
        filename: 'bundle.js'
    }
};