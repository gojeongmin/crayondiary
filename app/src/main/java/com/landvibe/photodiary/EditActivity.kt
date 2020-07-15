package com.landvibe.photodiary

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_edit.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class EditActivity : AppCompatActivity() {
    private val PICK_FROM_ALBUM = 1
    private val PICK_FROM_CAMERA = 2
    private var tempFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        attachEvents()

        if (intent.hasExtra("id")) {
            loadDiary()
        }
    }

    override fun onResume() {
        super.onResume()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this@EditActivity, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
    }

    private fun loadDiary() {
        val id = intent.getIntExtra("id", -1)
        val diary = AppDatabase.instance.diaryDao().get(id)
    }

    private fun attachEvents() {
        edit_back_button.setOnClickListener {
            finish()
        }

        edit_done_button.setOnClickListener {
            save()
        }

        edit_photo_image.setOnClickListener {
            changePhoto()
        }
    }

    private fun changePhoto() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Change photo")

        val animals = arrayOf("Camera", "Gallery", "Delete", "Cancel")
        builder.setItems(animals) { dialog, which ->
            when (which) {
                0 -> {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                    try {
                        tempFile = createImageFile()
                    } catch (e: IOException) {
                        Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                        finish()
                        e.printStackTrace()
                    }
                    tempFile?.let {
                        val photoUri = FileProvider.getUriForFile(this, "com.landvibe.photodiary.fileprovider", it)
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                        startActivityForResult(intent, PICK_FROM_CAMERA)
                    }
                }
                1 -> {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = MediaStore.Images.Media.CONTENT_TYPE
                    startActivityForResult(intent, PICK_FROM_ALBUM)
                }
                2 -> {
                    edit_photo_image.setImageResource(0)
                }
                3 -> {
                }
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun save() {
        AppDatabase.instance.diaryDao().insert(
            Diary(
                0,
                "",
                "aasdf",
                "2019-09-09",
                "Sunny"
            )
        )
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != 0) {
            return
        }

        if (requestCode == PICK_FROM_ALBUM) {
            val photoUri = data?.data
            var cursor: Cursor? = null
            try {
                val proj = arrayOf(MediaStore.Images.Media.DATA)

                assert(photoUri != null)
                cursor = contentResolver.query(photoUri, proj, null, null, null)

                assert(cursor != null)
                val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

                cursor.moveToFirst()

                tempFile = File(cursor.getString(column_index))

            } finally {
                cursor?.close()
            }
            setImage()
        } else if (requestCode == PICK_FROM_CAMERA) {
            setImage()
        }
    }

    private fun setImage() {
        val options = BitmapFactory.Options()
        val originalBm = BitmapFactory.decodeFile(tempFile?.absolutePath, options)
        edit_photo_image.setImageBitmap(originalBm)

    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("HHmmss").format(Date())
        val imageFileName = "photo_" + timeStamp + "_"
        val storageDir = File("${MainApplication.context.filesDir}/photodiary/")
        if (!storageDir.exists()) storageDir.mkdirs()
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != 0) {
            return
        }

        if (grantResults.none { i -> i == PackageManager.PERMISSION_GRANTED }) {
            ActivityCompat.requestPermissions(this@EditActivity, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
        }
    }
}