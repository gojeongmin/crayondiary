package com.landvibe.photodiary


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_view.*

class ViewActivity : Activity() {
    var diary: Diary? = null
    private lateinit var adapter: ViewContentRecyclerViewAdapter
    private lateinit var layoutManager: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //사용할 레이아웃 설정
        setContentView(R.layout.activity_view)
        //화면 생성 후 버튼 이벤트 추가
        attachButtonEvents()
        initRecyclerView()
        //메모 정보를 불러온다
        loadView()
    }

    private fun initRecyclerView() {
        adapter = ViewContentRecyclerViewAdapter()
        view_content_text.adapter = adapter
        layoutManager = GridLayoutManager(this, 10)
        view_content_text.layoutManager = layoutManager
    }

    //메모 불러오기
    private fun loadView() {
        val diaryId = intent.getIntExtra("id", -1)
        //메모 id는 intent를 통해 전달되는데 아이디가 없는 경우 토스트 노출하고 화면 종료하도록 구현
        if (diaryId == -1) {
            Toast.makeText(this, "메모 ID 값을 잘 전달받지 못했습니다", Toast.LENGTH_SHORT).show()
            finish()
        }

        diary = AppDatabase.instance.diaryDao().get(diaryId)
        diary?.let {
            PhotoUtils.setPhoto(view_photo_image, it.photoFileId)
            view_date_text.text = it.date
            view_weather_text.text = it.weather
            val content = it.content.split("")
            adapter.items.addAll(content)
            adapter.notifyDataSetChanged()
        } ?: run {
            Toast.makeText(this, "메모가 없습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun share() {
//        val share = Intent(Intent.ACTION_SEND)
//        share.type = "image/png"
//        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(path))
//        startActivity(Intent.createChooser(share, "Share Image"))
    }

    private fun attachButtonEvents() {
        //activity_view의 view_back_button를 누를 때 현재 화면 종료
        view_back_button.setOnClickListener { finish() }
        //activity_view의 view_delete_button를 누를 때 메모 삭제
        view_modify_button.setOnClickListener {
            val modifyIntent = Intent(this, EditActivity::class.java)
            modifyIntent.putExtra("id", intent.getIntExtra("id", -1))
            startActivity(modifyIntent)
            finish()
        }

        view_save_button.setOnClickListener {
            val path = PhotoUtils.toGallery(view_wrap_layout)
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder
                .setTitle("Screenshot Saved.")
                .setMessage("Share it?")
                .setPositiveButton("OK") { _, _ ->
                    val share = Intent(Intent.ACTION_SEND)
                    share.type = "image/png"
                    share.putExtra(Intent.EXTRA_STREAM, Uri.parse(path))
                    startActivity(Intent.createChooser(share, "Share Image"))
                }
                .setCancelable(true)
                .show()
        }
    }
}

