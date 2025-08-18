package com.mstf.note_app.service

import com.mstf.note_app.controller.NoteController.NoteRequest
import com.mstf.note_app.database.model.Note
import com.mstf.note_app.database.repository.NoteRepository
import com.mstf.note_app.util.exception.NotesNotFoundException
import jakarta.validation.Valid
import org.bson.types.ObjectId
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody
import java.time.Instant

@Service
class NoteService(
    private val noteRepository: NoteRepository
) {
    fun save(
        @Valid @RequestBody body: NoteRequest,
    ): Note {
        val ownerId = SecurityContextHolder.getContext().authentication.principal as String
        val note = noteRepository.save(
            Note(
                id = body.id?.let { ObjectId(it) } ?: ObjectId.get(),
                title = body.title,
                content = body.content,
                color = body.color,
                createdAt = Instant.now(),
                ownerId = ObjectId(ownerId),
            )
        )
        return note
    }

    fun findByOwnerId(): List<Note> {
        val ownerId = SecurityContextHolder.getContext().authentication.principal as String
        return noteRepository.findByOwnerId(ObjectId(ownerId))
    }

    fun deleteById(id: String) {
        val note = noteRepository.findById(ObjectId(id)).orElseThrow {
            NotesNotFoundException(id)
        }
        val ownerId = SecurityContextHolder.getContext().authentication.principal as String
        if (note.ownerId.toHexString() == ownerId) {
            noteRepository.deleteById(ObjectId(id))
        }
    }
}