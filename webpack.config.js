const path = require('path'); 
module.exports = {
    module: {
        rules: [
                {
                    test: /\.(css)$/,
                    use: ['style-loader', 'css-loader'],
                },
        ],
    },
    entry: './frontend/src/application.js',
    output: {
        path: path.resolve(__dirname, 'src', 'main', 'resources', 'static','js'),
        filename: 'bundle.js'
    }
};