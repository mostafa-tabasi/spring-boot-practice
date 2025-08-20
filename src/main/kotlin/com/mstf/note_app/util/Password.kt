package com.mstf.note_app.util

import jakarta.validation.Constraint
import jakarta.validation.Payload
import jakarta.validation.constraints.Pattern
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [])
@Pattern(
    regexp = "^(?=.*[\\d!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?])(.{9,})$",
    message = "Password must consists of at least 9 characters and a special character or digit"
)
annotation class Password(
    val message: String = "Password must consists of at least 9 characters and a special character or digit",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
