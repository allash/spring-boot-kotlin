package com.home.piperbike.api

import org.springframework.transaction.annotation.Transactional

@Transactional(rollbackFor = [Throwable::class], readOnly = false)
abstract class BaseController