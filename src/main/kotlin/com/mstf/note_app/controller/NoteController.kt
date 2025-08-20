package com.mstf.note_app.controller

import com.mstf.note_app.database.model.Note
import com.mstf.note_app.service.NoteService
import com.mstf.note_app.util.Password
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.hibernate.validator.constraints.Length
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
@RequestMapping("/notes")
class NoteController(
    private val noteService: NoteService,
) {

    data class NoteRequest(
        val id: String?,
        // custom pattern validation or author; must be two separate words as Name and Family name.
        @field:Pattern(
            regexp = "\\b[a-zA-Z]+\\b(?:\\s+\\b[a-zA-Z]+\\b)+",
            message = "Author must at least consist of a first and last name"
        )
        val author: String,
        @field: NotBlank(message = "Title can't be blank")
        val title: String,
        @field: Length(
            min = 5,
            max = 500,
            message = "Content must be between 5 and 500 characters",
        )
        val content: String,
        val color: Long,
        @field: Password
        val password: String,
    )

    data class NoteResponse(
        val id: String,
        val title: String,
        val content: String,
        val color: Long,
        val createdAt: Instant,
    )

    @PostMapping
    fun save(
        @Valid @RequestBody body: NoteRequest,
    ): NoteResponse {
        val note = noteService.save(body)
        return note.toResponse()
    }

    @GetMapping
    fun findByOwnerId(): List<NoteResponse> {
        val notes = noteService.findByOwnerId()
        return notes.map { it.toResponse() }
    }

    @DeleteMapping(path = ["/{id}"])
    fun deleteById(
        @PathVariable id: String,
    ) = noteService.deleteById(id)
}

private fun Note.toResponse(): NoteController.NoteResponse {
    return NoteController.NoteResponse(
        id = id.toHexString(),
        title = title,
        content = content,
        color = color,
        createdAt = createdAt,
    )
}