'use strict';
/*eslint no-invalid-this:0*/
import crypto from 'crypto';
mongoose.Promise = require('bluebird');
import mongoose, { Schema } from 'mongoose';
import config from '../../config/environment';

var PeopleSchema = new Schema({
    firstname: { type: String, default: null },
    lastname: { type: String, default: null },
    date_of_birth: { type: Date, default: null },
    description: { type: String, default: null },
    relationship: { type: String, default: 'friend', lowercase: true },
    phone_number: { type: String, default: null },
    address: {
        street: { type: String, default: null },
        city: { type: String, default: null },
        state: { type: String, default: null },
        zipcode: { type: String, default: null },
        country: { type: String, default: null }
    },
    user_id: { type: mongoose.Schema.Types.ObjectId, ref: 'User' }
});

PeopleSchema.pre('save', function(next) {
    if (this.isModified('relationship')) {
        console.log('I got updated: ', this.relationship);
        console.log('indexOf: ', config.relationshipTypes.indexOf(this.relationship));
    }
    if (this.isModified('relationship') && config.relationshipTypes.indexOf(this.relationship) == -1) {
        console.log("I got here");
        return next(new Error('You must specify a valid relationship'));
    }
    return next();
});

export default mongoose.model('People', PeopleSchema);