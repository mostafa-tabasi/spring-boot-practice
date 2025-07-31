package com.mstf.note_app.util.exception

import java.lang.RuntimeException

class NotesNotFoundException(
    id: String,
): RuntimeException(
    "Any notes with ID $id not found."
)