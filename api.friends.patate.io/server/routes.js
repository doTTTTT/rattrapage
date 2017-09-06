'use strict';

import errors from './components/errors';
import path from 'path';

export default function(app) {
    // Insert routes below
    app.use('/people', require('./api/people'));
    app.use('/users', require('./api/user'));

    app.use('/auth', require('./auth').default);

    app.use(function(err, req, res, next) {
        let myError = { status: 500, message: err.message || 'Internal server error' };
        switch (err.name) {
            case 'UnauthorizedError':
                myError = { status: 401, message: 'invalid token...' };
                break;
            case 'ValidationError':
                myError.status = 422;
                break;
            default:
                console.log('debug ==> ', err);
                break;
        }
        return res.status(myError.status).send(myError);
    });
}