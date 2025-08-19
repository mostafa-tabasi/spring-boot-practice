package com.mstf.note_app

import com.mstf.note_app.config.NotesConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(
    NotesConfig::class
)
class NoteAppApplication

fun main(args: Array<String>) {
    runApplication<NoteAppApplication>(*args)
}
