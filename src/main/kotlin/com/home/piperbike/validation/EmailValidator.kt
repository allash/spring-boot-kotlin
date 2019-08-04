package com.home.piperbike.validation

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class EmailValidator : ConstraintValidator<Email, String> {
    companion object {
        private val HIBERNATE_EMAIL_VALIDATOR = org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator()
        val INSTANCE = EmailValidator()
        fun isValid(value: String): Boolean = INSTANCE.isValid(value, null)
    }

    override fun initialize(p0: Email?) {}
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        return when {
            value == null -> true
            value.isBlank() -> false
            else -> HIBERNATE_EMAIL_VALIDATOR.isValid(value, context)
        }
    }
}