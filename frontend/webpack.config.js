var path = require("path");
var HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
    entry: "./src/app.js",
    output: {
        path: path.join(__dirname, "build"),
        filename: "bundle.js"
    },
    module: {
        loaders: [
            {
                test: /\.css$/,
                loader: "style!css"
            },
            {
                test: /\.js$/,
                exclude: /node_modules/,
                loader: 'babel-loader',
                query: {
                    presets: ['es2015']
                }
            }
        ]
    },
    plugins: [
        new HtmlWebpackPlugin({
            title: 'RODAR',
            filename: path.join(__dirname, "build", "index.html"),
            template: path.join(__dirname, "src", "index.html")
        })
    ]
};