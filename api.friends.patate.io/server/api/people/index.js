'use strict';

import { Router } from 'express';
import * as controller from './people.controller';
import * as auth from '../../auth/auth.service';

var router = new Router();

// TODO: Add /me routes
// TODO: Update permissions, everyone logged can delete / see everything.. nice ~

router.get('/', auth.hasRole('admin'), controller.index);
router.get('/user/me', auth.isAuthenticated(), controller.showUserMe);
router.get('/user/:id', auth.hasRole('admin'), controller.showUser);
router.get('/me/:id', auth.isAuthenticated(), controller.showMe);
router.get('/:id', auth.hasRole('admin'), controller.show);

router.post('/', auth.isAuthenticated(), controller.create);

router.put('/address/me/:id', auth.isAuthenticated(), controller.updateAddressMe);
router.put('/address/:id', auth.hasRole('admin'), controller.updateAddress);
router.put('/me/:id', auth.isAuthenticated(), controller.updateMe);
router.put('/:id', auth.hasRole('admin'), controller.update);

router.delete('/me/:id', auth.isAuthenticated(), controller.destroyMe);
router.delete('/:id', auth.hasRole('admin'), controller.destroy);

module.exports = router;