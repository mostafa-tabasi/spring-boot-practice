package com.mstf.note_app.controller

import com.mstf.note_app.service.ImageService
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.FileNotFoundException
import java.io.IOException

@RestController
@RequestMapping("/images")
class ImageController(
    private val imageService: ImageService,
) {

    data class UploadResponse(
        val imageId: String,
        val originalName: String,
        val size: Long,
    )

    @PostMapping("/upload")
    fun uploadImage(
        @RequestParam("file") file: MultipartFile,
    ): ResponseEntity<UploadResponse> {
        // in real scenarios we get the owner ID from JWT token
        val ownerId = "0000"

        try {
            val metadata = imageService.uploadImage(file, ownerId)
            return ResponseEntity
                .ok(
                    UploadResponse(
                        metadata.id.toHexString(),
                        metadata.originalName,
                        metadata.size
                    )
                )
        } catch (e: IOException) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().build()
        }
    }

    @GetMapping("/{imageId}")
    fun downloadImage(
        @PathVariable imageId: String
    ): ResponseEntity<Resource> {
        try {
            val metadata = imageService.getImageMetadata(imageId)
            val resource = imageService.getImageResource(imageId)

            return ResponseEntity
                .ok()
                .header(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + metadata.originalName + "\""
                )
                .contentType(MediaType.parseMediaType(metadata.mimeType))
                .contentLength(metadata.size)
                .body(resource)
        } catch (e: IOException) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        } catch (e: FileNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }
}