'use strict';

import People from './people.model';
import User from '../user/user.model';
import { signToken } from '../../auth/auth.service';
import async from 'async';
import mongoose from 'mongoose';
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
    console.log('handleError')
    statusCode = statusCode || 422;
    return function(err) {
        console.log("debug error ==> ", err);
        let err_obj = err;
        if (typeof err === "string") {
            err_obj = { message: err.message || err, status: statusCode };
        } else {
            err_obj = { message: err.message || "Unable to process your request", status: err.status || statusCode };
        }
        return res.status(statusCode).send(err_obj);
    };
}

function respondWithResult(res, statusCode) {
    console.log('responseWithResult')
    statusCode = statusCode || 200;
    return function(entity) {
        if (entity) {
            return res.status(statusCode).json(entity);
        }
        return null;
    };
}

/**
 * Get list of people
 * restriction: 'admin'
 */
export function index(req, res) {
    return People.find({}).exec()
        .then(people => {
            res.status(200).json(people);
        })
        .catch(handleError(res));
}

/**
 * Creates a new people
 */
export function create(req, res) {
    delete req.body._id;

    if (req.body.user_id === undefined || !mongoose.Types.ObjectId.isValid(req.body.user_id)) {
        return handleError(res, 422)({ message: 'You must specify a valid user_id', status: 422 });
    }
    return User.findById(req.body.user_id).exec()
        .then(handleEntityNotFound(res, "user"))
        .then(user => {
            if (user === null) {
                return null;
            }
            return People.create(req.body)
                .then(people => {
                    user.people.push(people._id);
                    return user.save()
                        .then(() => {
                            return respondWithResult(res, 201)(people);
                        })
                        .catch(handleError(res));
                })
                .catch(handleError(res));
        })
        .catch(handleError(res));
}

export function showUser(req, res) {
    if (!mongoose.Types.ObjectId.isValid(req.params.id)) {
        return handleError(res, 422)({ message: 'You must specify a valid user id', status: 422 });
    }
    return User.findById(req.params.id).exec()
        .then(handleEntityNotFound(res))
        .then(user => {
            if (user === null) {
                return null;
            }
            return People.find({ user_id: user._id }).exec()
                .then(respondWithResult(res))
                .catch(handleError(res));
        })
        .catch(handleError(res));
}

/**
 * Get a single people
 */
export function show(req, res) {
    if (!mongoose.Types.ObjectId.isValid(req.params.id)) {
        return handleEntityNotFound(res, 'people')(null);
    }
    return People.findById(req.params.id).exec()
        .then(handleEntityNotFound(res, 'people'))
        .then(people => {
            if (people === null) {
                return null;
            }
            return respondWithResult(res)(people);
        })
        .catch(handleError(res));
}

export function updateAddress(req, res) {
    if (!mongoose.Types.ObjectId.isValid(req.params.id)) {
        return handleEntityNotFound(res, 'people')(null);
    }
    return People.findById(req.params.id).exec()
        .then(handleEntityNotFound(res, 'people'))
        .then(people => {
            if (people === null) {
                return null;
            }
            let address = {
                type: null,
                street: null,
                city: null,
                state: null,
                zipcode: null,
                country: null
            };
            address = _.merge(address, people.address);
            address = _.merge(address, req.body);
            people.address = address;
            return people.address.save()
                .then(() => {
                    return respondWithResult(res)(people);
                })
                .catch(handleError(res))
        })
        .catch(handleError(res));
}

export function update(req, res) {
    delete req.body._id;
    delete req.body.address;
    delete req.body.user_id;

    if (!mongoose.Types.ObjectId.isValid(req.params.id)) {
        return handleEntityNotFound(res, 'people')(null);
    }
    return People.findById(req.params.id).exec()
        .then(handleEntityNotFound(res, 'people'))
        .then(people => {
            if (people === null) {
                return null;
            }
            let updated = _.merge(people, req.body);
            return updated.save()
                .then(respondWithResult(res))
                .catch(handleError(res))
        })
        .catch(handleError(res));
}

export function destroy(req, res) {
    if (!mongoose.Types.ObjectId.isValid(req.params.id)) {
        return handleEntityNotFound(res, 'people')(null);
    }
    return People.findByIdAndRemove(req.params.id).exec()
        .then(handleEntityNotFound(res, 'people'))
        .then(people => {
            if (people === null) {
                return null;
            }
            return res.status(204).send();
        })
        .catch(handleError(res));
}

export function showUserMe(req, res) {
    req.params = { id: req.user._id }
    return showUser(req, res);
}

export function showMe(req, res) {
    if (req.user.role != 'admin') {
        if (!mongoose.Types.ObjectId.isValid(req.params.id)) {
            return handleEntityNotFound(res, 'people')(null);
        }
        return People.findOne({ _id: req.params.id, user_id: req.user._id }).exec() // We will not allow a user to try to find valid people id!
            .then(handleEntityNotFound(res, 'people'))
            .then(people => {
                if (people === null) {
                    return null;
                }
                return respondWithResult(res)(people);
            })
            .catch(handleError(res));
    } else {
        return show(req, res);
    }
}

export function updateAddressMe(req, res) {
    if (req.user.role != 'admin') {
        if (!mongoose.Types.ObjectId.isValid(req.params.id)) {
            return handleEntityNotFound(res, 'people')(null);
        }
        return People.findOne({ _id: req.params.id, user_id: req.user._id }).exec() // We will not allow a user to try to find valid people id!
            .then(handleEntityNotFound(res, 'people'))
            .then(people => {
                if (people === null) {
                    return null;
                }
                let address = {
                    type: null,
                    street: null,
                    city: null,
                    state: null,
                    zipcode: null,
                    country: null
                };
                people.address = _.merge(people.address, req.body);
                return people.save()
                    .then(() => {
                        return respondWithResult(res)(people);
                    })
                    .catch(handleError(res))
            })
            .catch(handleError(res));
    } else {
        return updateAddress(req, res);
    }
}

export function updateMe(req, res) {
    if (req.user.role != 'admin') {
        delete req.body._id;
        delete req.body.address;
        delete req.body.user_id;
        if (!mongoose.Types.ObjectId.isValid(req.params.id)) {
            return handleEntityNotFound(res, 'people')(null);
        }
        return People.findById(req.params.id).exec() // We will not allow a user to try to find valid people id!
            .then(handleEntityNotFound(res, 'people'))
            .then(people => {
                if (people === null) {
                    return null;
                }
                var updated = _.merge(people, req.body);
                return updated.save(err => {
                    if (err) {
                        return handleError(res)(err);
                    } else {
                        return respondWithResult(res)(updated);
                    }
                })
            })
            .catch(handleError(res));
    } else {
        return update(req, res);
    }
}

export function destroyMe(req, res) {
    if (req.user.role != 'admin') {
        if (!mongoose.Types.ObjectId.isValid(req.params.id)) {
            return handleEntityNotFound(res, 'people')(null);
        }
        return People.findOne({ _id: req.params.id, user_id: req.user._id }).exec() // We will not allow a user to try to find valid people id!
            .then(handleEntityNotFound(res, 'people'))
            .then(people => {
                if (people === null) {
                    return null;
                }
                return User.findByIdAndUpdate(people.user_id, { "$pull": { "people": people._id } }, { new: true }).exec()
                    .then(user => {
                        if (user === null) {
                            return null;
                        }
                        console.log('user ==> ', user._id);
                        return people.remove()
                            .then(() => {
                                console.log('ok')
                                return res.status(204).send();
                            })
                            .catch(handleError(res));
                    })
                    .catch(handleError(res));
            })
            .catch(handleError(res));
    } else {
        return destroy(req, res);
    }
}