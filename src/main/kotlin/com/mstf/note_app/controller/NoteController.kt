package com.mstf.note_app.controller

import com.mstf.note_app.database.model.Note
import com.mstf.note_app.service.NoteService
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/notes")
class NoteController(
    private val noteService: NoteService,
) {

    data class NoteRequest(
        val id: String?,
        @field: NotBlank(message = "Title can't be blank")
        val title: String,
        @field: NotBlank(message = "Content can't be blank")
        val content: String,
        val color: Long,
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