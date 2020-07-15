package com.landvibe.photodiary

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.view.View
import android.widget.ImageView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class PhotoUtils {
    companion object {
        private fun getFilePath(id: String): String {
            return "${MainApplication.context.filesDir}/photo/$id"
        }

        fun delete(id: String) {
            File(getFilePath(id)).delete()
        }

        fun setPhoto(view: ImageView, id: String) {
            if (id.isNotEmpty()) {
                view.setImageURI(Uri.parse(getFilePath(id)))
            }
        }

        fun savePhoto(diary: Diary, imageView: ImageView) {
            val oldFileId = diary.photoFileId
            val drawable = imageView.drawable as? BitmapDrawable
            drawable?.bitmap?.let {
                diary.photoFileId = saveImage(it, diary.id)
                File(getFilePath(oldFileId)).delete()
            }
        }

        private fun saveImage(bitmap: Bitmap, id: Int): String {
            val bytes = ByteArrayOutputStream()
            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 120, 120, true)
            scaledBitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)
            val photoDirectory = File(MainApplication.context.filesDir, "/photo")
            if (!photoDirectory.exists()) {
                photoDirectory.mkdirs()
            }

            try {
                val fileName = "${id}_${Date().time}.png"
                val f = File(photoDirectory, fileName)
                f.createNewFile()
                val fo = FileOutputStream(f)
                fo.write(bytes.toByteArray())
                fo.close()
                return fileName
            } catch (e1: IOException) {
                e1.printStackTrace()
            }

            return ""
        }

        fun toGallery(layout: View): String {
            val bitmap = capture(layout)

            val bytes = ByteArrayOutputStream()
            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 640, 960, true)
            scaledBitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)
            val photoDirectory = File(Environment.getExternalStorageDirectory(), "/crayondiary")
            if (!photoDirectory.exists()) {
                photoDirectory.mkdirs()
            }

            return try {
                val f = File(photoDirectory, "${Date().time}.png")
                f.createNewFile()
                val fo = FileOutputStream(f)
                fo.write(bytes.toByteArray())
                fo.close()
                MediaScannerConnection.scanFile(MainApplication.context, arrayOf(f.absolutePath), null, null)
                f.absolutePath
            } catch (e1: IOException) {
                e1.printStackTrace()
                ""
            }
        }

        private fun capture(v: View): Bitmap {
            v.isDrawingCacheEnabled = true
            v.buildDrawingCache(true)
            val b = Bitmap.createBitmap(v.drawingCache)
            v.isDrawingCacheEnabled = false
            return b
        }
    }
}