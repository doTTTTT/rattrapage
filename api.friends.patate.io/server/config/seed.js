/**
 * Populate DB with sample data on server start
 * to disable, edit config/environment/index.js, and set `seedDB: false`
 */

'use strict';
import User from '../api/user/user.model';
import config from './environment/';

export default function seedDatabaseIfNeeded() {
  if(config.seedDB) {
    User.findOne({email: 'admin@patate.io'})
      .then(user => {
        if (user === undefined) {
          User.create({
            provider: 'local',
            role: 'admin',
            name: 'Admin',
            email: 'admin@patate.io',
            password: 'admin'
          });
        }
      })
      .catch(err => {
        console.log('error populating users', err);
      });
    User.findOne({email: 'test@patate.io'})
      .then(user => {
        if (user === undefined) {
          User.create({
            provider: 'local',
            name: 'Test User',
            email: 'test@patate.io',
            password: 'test'
          });
        }
      })
      .catch(err => {
        console.log('error populating users', err);
      });
  }
}
