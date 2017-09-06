/**
 * Express configuration
 */

'use strict';

import express from 'express';
import favicon from 'serve-favicon';
import morgan from 'morgan';
import shrinkRay from 'shrink-ray';
import bodyParser from 'body-parser';
import methodOverride from 'method-override';
import cookieParser from 'cookie-parser';
import errorHandler from 'errorhandler';
import path from 'path';
import lusca from 'lusca';
import config from './environment';
import passport from 'passport';
import session from 'express-session';
import connectMongo from 'connect-mongo';
import mongoose from 'mongoose';
import cors from 'cors';
var MongoStore = connectMongo(session);

export default function(app) {
    var env = app.get('env');

    app.use(cors());
    app.use(morgan('dev'));

    app.use(shrinkRay());
    app.use(bodyParser.urlencoded({ extended: false }));
    app.use(bodyParser.json());
    app.use(methodOverride());
    app.use(passport.initialize());


    if (env === 'development' || env === 'test') {
        app.use(errorHandler()); // Error handler - has to be last
    }
}