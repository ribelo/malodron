const webpack = require('webpack');
const path = require('path');

const BUILD_DIR = path.resolve(__dirname, 'resources/public', 'js');
const MAIN_DIR = path.resolve(__dirname, 'dev-resources/public', 'js');

const config = {
    entry: `${MAIN_DIR}/main.js`,
    output: {
        path: BUILD_DIR,
        filename: 'bundle.js'
    },
};

module.exports = config;