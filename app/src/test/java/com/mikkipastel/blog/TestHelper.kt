package com.mikkipastel.blog

import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStream
import java.io.InputStreamReader

class TestHelper {
    fun getStringFromFile(filePath: String): String {
        val fileName = System.getProperty("user.dir") + "/src/test/data/"
        val inputStream = FileInputStream(fileName + filePath)
        val outputStream = convertStreamToString(inputStream)
        inputStream.close()
        return outputStream
    }

    private fun convertStreamToString(inputStream: InputStream): String {
        val reader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            stringBuilder.append(line).append("\n")
        }
        reader.close()
        return stringBuilder.toString()
    }
}