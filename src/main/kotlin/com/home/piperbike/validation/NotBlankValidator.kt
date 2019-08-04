package com.home.piperbike.validation

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class NotBlankValidator : ConstraintValidator<NotBlank, String> {

    override fun initialize(p0: NotBlank?) {}

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        value ?: return true
        return value.isNotBlank()
    }
}