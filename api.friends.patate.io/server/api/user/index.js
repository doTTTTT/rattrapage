'use strict';

import { Router } from 'express';
import * as controller from './user.controller';
import * as auth from '../../auth/auth.service';

var router = new Router();

router.get('/', auth.hasRole('admin'), controller.index);
router.get('/me', auth.isAuthenticated(), controller.me);
router.get('/:id', auth.hasRole('admin'), controller.show);
router.put('/me', auth.isAuthenticated(), controller.updateMe);
router.put('/:id', auth.hasRole('admin'), controller.update);
router.post('/', controller.create);
router.delete('/delete/:id', auth.hasRole('admin'), controller.destroy);
router.delete('/:id', auth.isAuthenticated(), controller.disable);

module.exports = router;