package com.mstf.note_app.database.repository

import com.mstf.note_app.database.model.ImageMetadata
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface ImageMetadataRepository : MongoRepository<ImageMetadata, ObjectId> {
}