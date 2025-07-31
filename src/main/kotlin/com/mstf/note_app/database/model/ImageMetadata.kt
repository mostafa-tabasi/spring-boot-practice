package com.mstf.note_app.database.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("image_metadata")
data class ImageMetadata(
    val originalName: String,
    val storedName: String,
    val mimeType: String,
    val ownerId: String,
    val size: Long,
    val createdAt: Instant,
    @Id val id: ObjectId,
)
