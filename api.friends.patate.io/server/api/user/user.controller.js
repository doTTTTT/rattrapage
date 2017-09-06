'use strict';

import User from './user.model';
import { signToken } from '../../auth/auth.service';
import async from 'async';
import mongoose from 'mongoose';
import People from '../people/people.model';
import _ from 'lodash';

function validationError(res, statusCode) {
    statusCode = statusCode || 422;
    return function(err) {
        if (err._messaage != undefined) {
            err.message = err.message.substr(err._message.length + ": ".length);
        }
        return res.status(statusCode).send(err);
    };
}

function handleEntityNotFound(res, type = "entity") {
    return function(entity) {
        if (!entity) {
            let err = new Error("Entity not found");
            err.status = 404;
            err.message = "Unable to find your " + type;
            if (res) { return res.status(404).send(err); }
            return null;
        }
        return entity;
    }
}

function handleError(res, statusCode) {
    statusCode = statusCode || 500;
    return function(err) {
        console.log('debug err => ', err);
        return res.status(statusCode).send(err);
    };
}

function respondWithResult(res, statusCode) {
    statusCode = statusCode || 200;
    return function(entity) {
        if (entity) {
            return res.status(statusCode).json(entity);
        }
        return null;
    };
}

/**
 * Get list of users
 * restriction: 'admin'
 */
export function index(req, res) {
    return User.find({}, '-salt -password -email').populate('people').exec()
        .then(users => {
            res.status(200).json(users);
        })
        .catch(handleError(res));
}

export function createAdmin(req, res) {
    var newAdmin = new User(req.body);
    newAdmin.role = 'admin';
    newAdmin.save()
        .then(admin => {
            let token = signToken(admin._id, admin.role);
            delete admin.salt;
            delete admin.password;
            return respondWithResult(res, 201)({ user: admin, token: token });
        })
        .catch(validationError(res));
}

/**
 * Creates a new user
 */
export function create(req, res) {
    var newUser = new User(req.body);
    newUser.role = 'user';
    newUser.save()
        .then(function(user) {
            let token = signToken(user._id, user.role);
            delete user.salt;
            delete user.password;
            return respondWithResult(res, 201)({ user: user, token: token });
        })
        .catch(validationError(res));
}

/**
 * Get a single user
 */
export function show(req, res, next) {
    var userId = req.params.id;

    if (!mongoose.Types.ObjectId.isValid(userId)) {
        return handleError(res, 422)({ status: 422, message: 'You must specify a valid user id' });
    }

    return User.findById(userId).populate('people').exec()
        .then(user => {
            if (!user) {
                return res.status(404).end();
            }
            res.json(user.profile);
        })
        .catch(err => next(err));
}

export function update(req, res) {
    delete req.body._id;
    delete req.body.role;
    delete req.body.email;
    delete req.body.people;
    delete req.body.disabled;

    if (!mongoose.Types.ObjectId.isValid(req.params.id)) {
        return handleError(res, 422)({ status: 422, message: 'You must specify a valid user id' });
    }

    console.log('debug body => ', req.body);
    return User.findById(req.params.id).populate('people').exec()
        .then(handleEntityNotFound(res, 'user'))
        .then(user => {
            if (user === null) {
                return null;
            }
            let updated = _.merge(user, req.body)
            return updated.save()
                .then(respondWithResult(res))
                .catch(handleError(res))
        })
        .catch(handleError(res))
}


/**
 * Get my info
 */
export function me(req, res, next) {
    var userId = req.user._id;

    return User.findOne({ _id: userId }, '-salt -password').populate('people').exec()
        .then(user => {
            if (!user) {
                return res.status(401).end();
            }
            res.json(user);
        })
        .catch(err => next(err));
}

export function destroy(req, res) {
    if (!mongoose.Types.ObjectId.isValid(req.params.id)) {
        return handleEntityNotFound(res, 'user')(null);
    }
    return User.findById(req.params.id).exec()
        .then(handleEntityNotFound(res, 'user'))
        .then(user => {
            if (user === null) {
                return null;
            }
            return People.remove({ user_id: user._id }).exec()
                .then(() => {
                    return user.remove()
                        .then(() => {
                            return res.status(204).end();
                        })
                        .catch(handleError(res));
                })
                .catch(handleError(res));
        })
        .catch(handleError(res));
}

export function disable(req, res) {
    if (!mongoose.Types.ObjectId.isValid(req.params.id)) {
        return handleEntityNotFound(res, 'user')(undefined);
    }
    return User.findById(req.params.id).exec()
        .then(handleEntityNotFound(res, 'user'))
        .then(user => {
            if (user === null) {
                return null;
            }
            user.disabled = true;
            user.save()
                .then(() => {
                    return res.status(204).end();
                })
                .catch(handleError(res));
        })
        .catch(handleError(res));
}

export function updateMe(req, res) {
    req.params = { id: req.user._id };
    return update(req, res);
}