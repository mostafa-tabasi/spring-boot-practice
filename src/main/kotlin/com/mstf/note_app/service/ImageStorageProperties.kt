package com.mstf.note_app.service

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties(prefix = "app.image-storage")
@Component
data class ImageStorageProperties(
    // in real life scenarios it's better to not store images in the project root folder
    val basePath: String = "./images",
    val allowedMimeTypes: List<String> = arrayListOf(
        "image/jpeg",
        "image/png",
        "image/webp",
        "image/gif"
    ),
)