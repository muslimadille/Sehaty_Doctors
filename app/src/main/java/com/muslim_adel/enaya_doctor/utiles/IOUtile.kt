package com.muslim_adel.enaya_doctor.utiles

import java.io.File
import java.io.IOException
import java.io.RandomAccessFile


object IOUtile {
    @Throws(IOException::class)
    fun readFileAsByteArray(file: File): ByteArray {
        // Open file
        val f = RandomAccessFile(file, "r")
        try {
            // Get and check length
            val longlength = f.length()
            val length = longlength.toInt()
            if (length.toLong() != longlength)
                throw IOException("File size >= 2 GB")
            // Read file and return data
            val data = ByteArray(length)
            f.readFully(data)
            return data
        } finally {
            f.close()
        }
    }
    fun getFileType(fileName: String): String {
        return when {
            fileName.contains(".pdf")
                    || fileName.contains(".doc")
                    || fileName.contains(".docx") -> "text"
            fileName.contains(".mp3") -> "voice"
            fileName.contains(".png") || fileName.contains(".jpg") -> "img"
            else -> ""
        }
    }
}