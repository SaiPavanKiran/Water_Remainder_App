package com.rspk.water_remainder_app.utils

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun actionUpdate(action:String,selection:Int?=null,context: Context){
    CoroutineScope(Dispatchers.IO).launch {
        context.contentResolver?.query(
            MediaStore.Files.getContentUri("external"),
            arrayOf(
                MediaStore.MediaColumns._ID,
                MediaStore.MediaColumns.DISPLAY_NAME
            ),
            "${MediaStore.MediaColumns.DISPLAY_NAME} LIKE ?",
            arrayOf("daily_list %"),
            null
        )?.use { cursor ->
            while(cursor.moveToNext()){
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
                val id = cursor.getLong(columnIndex)
                val uri = ContentUris.withAppendedId(MediaStore.Files.getContentUri("external"),id)
                withContext(Dispatchers.IO){
                    context.contentResolver?.openInputStream(uri)?.use { inputStream ->
                        val lines = inputStream.bufferedReader().readLines().toMutableList()

                        if (lines.isNotEmpty()) {
                            val indexToUpdate = selection?.let { lines.size - it } ?: (lines.size - 1)

                            if (indexToUpdate in lines.indices) {
                                // Update the specified line
                                val lineToUpdate = lines[indexToUpdate]
                                val parts = lineToUpdate.substringBeforeLast(":")
                                lines.removeAt(indexToUpdate)
                                val updatedLine = "${parts}:${action}"
                                lines.add(indexToUpdate, updatedLine)

                                // Write updated content back to the file with newline characters
                                context.contentResolver.openOutputStream(uri, "wt")?.use { outputStream ->
                                    outputStream.bufferedWriter().use { writer ->
                                        writer.write(lines.joinToString("\n") +"\n")  // Add newline at the end
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}