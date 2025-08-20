package com.mstf.note_app.util

import com.mstf.note_app.util.exception.NotesNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class NotesExceptionHandler {

    @ExceptionHandler(NotesNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun onNotesNotFound(e: NotesNotFoundException) = mapOf(
        "errorCode" to "NOTE_NOT_FOUND",
        "message" to e.message
    )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun onValidationFailed(e: MethodArgumentNotValidException): ResponseEntity<Map<String, Any>> {
        val map = mutableMapOf<String, Any>()
        e.bindingResult.fieldErrors.forEach { error ->
            map[error.field] = error.defaultMessage ?: "Validation failed"
        }

        return ResponseEntity
            .badRequest()
            .body(map)
    }
}
