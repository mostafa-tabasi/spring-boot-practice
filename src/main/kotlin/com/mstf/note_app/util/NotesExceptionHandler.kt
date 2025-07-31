package com.mstf.note_app.util

import com.mstf.note_app.util.exception.NotesNotFoundException
import org.springframework.http.HttpStatus
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
}
