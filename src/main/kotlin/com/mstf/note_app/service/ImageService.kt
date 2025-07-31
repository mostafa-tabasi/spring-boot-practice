package com.mstf.note_app.service

import com.mstf.note_app.database.model.ImageMetadata
import com.mstf.note_app.database.repository.ImageMetadataRepository
import org.bson.types.ObjectId
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.FileNotFoundException
import java.io.IOException
import java.time.Instant

@Service
class ImageService(
    private val properties: ImageStorageProperties,
    private val repository: ImageMetadataRepository,
    private val storageService: LocalImageStorageService,
) {

    @Throws(IOException::class, IllegalArgumentException::class)
    fun uploadImage(file: MultipartFile, ownerId: String): ImageMetadata {
        validateImage(file)

        val inputStream = file.inputStream
        val storagePath = storageService.storeFile(inputStream, file.originalFilename.toString())

        val metadata = ImageMetadata(
            file.originalFilename.toString(),
            storagePath,
            file.contentType.toString(),
            ownerId,
            file.size,
            Instant.now(),
            ObjectId.get(),
        )

        return repository.save(metadata)
    }

    private fun validateImage(file: MultipartFile) {
        if (file.isEmpty) {
            throw IllegalArgumentException("File is empty.")
        }

        val mimeType = file.contentType
        if (mimeType == null || !properties.allowedMimeTypes.contains(mimeType)) {
            throw IllegalArgumentException("Invalid mime type.")
        }
    }

    @Throws(IOException::class)
    fun getImageResource(imageId: String): Resource {
        val metadata = getImageMetadata(imageId)
        return storageService.getFileResource(metadata.storedName)
    }

    @Throws(IOException::class, FileNotFoundException::class)
    fun getImageMetadata(imageId: String): ImageMetadata {
        val objectId = ObjectId(imageId)
        return repository.findById(objectId).orElseThrow {
            FileNotFoundException("File not found")
        }
    }

}