package com.mstf.note_app.service

import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.StreamUtils
import java.io.*
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.time.LocalDate
import java.util.*

@Service
class LocalImageStorageService(
    private val properties: ImageStorageProperties,
) {

    private var rootPath: Path = Paths.get(properties.basePath)

    @Throws(IOException::class)
    fun storeFile(inputStream: InputStream, originalName: String): String {
        val today: LocalDate = LocalDate.now()
        // example: ./2025/07/22
        val dateDirectory: Path = rootPath.resolve(
            today.year.toString() + File.separator +
                    // %02d used for adding padding to the month value if needed, like 12->12 or 5->05
                    String.format("%02d", today.monthValue) + File.separator +
                    String.format("%02d", today.dayOfMonth)
        )

        Files.createDirectories(dateDirectory)

        val ext: String = getFileExtension(originalName)
        val storedName = "${UUID.randomUUID()}${(if (ext.isEmpty()) "" else ".$ext")}"
        val filePath: Path = dateDirectory.resolve(storedName)

        val outputStream: OutputStream = Files.newOutputStream(filePath, StandardOpenOption.CREATE_NEW)
        StreamUtils.copy(inputStream, outputStream)

        // relativize will remove the parts that are the same in the address. for example:
        // rootPath = ./a/b/c
        // filePath = ./a/b/c/d
        // relativize => /d
        return rootPath.relativize(filePath).toString()
    }

    @Throws(IOException::class)
    fun getFileResource(storedPath: String): Resource {
        val filePath = rootPath.resolve(storedPath).normalize().toAbsolutePath()
        val normalizedRoot = rootPath.normalize().toAbsolutePath()

        // for security check like if the user tries to send weird address (!)
        if (!filePath.startsWith(normalizedRoot)) {
            throw SecurityException("Access denied")
        }

        if (!Files.exists(filePath)) {
            throw FileNotFoundException("File not found")
        }

        return UrlResource(filePath.toUri())
    }

    private fun getFileExtension(fileName: String): String {
        val lastDot: Int = fileName.lastIndexOf('.')
        return if (lastDot == -1) "" else fileName.substring(lastDot + 1)
    }
}