package com.landvibe.photodiary

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_main.view.*

class MainRecyclerViewAdapter(
    private val context: Context,
    val items: MutableList<Diary> = mutableListOf()
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /*onCreateViewHolder : RecyclerView의 행을 표시하는데 사용되는 layout xml을 가져옴*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MainRecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_main, parent, false))
    }

    /*RecyclerView의 행에 보여질 ImageView와 TextView 설정*/
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        bindDefaultView(holder as MainRecyclerViewHolder, position)
    }

    private fun bindDefaultView(holder: MainRecyclerViewHolder, position: Int) {
        val item = items[position]
        holder.itemView.item_main_date_text.text = item.date

        PhotoUtils.setPhoto(holder.itemView.item_main_photo_image, item.photoFileId)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ViewActivity::class.java)
            intent.putExtra("id", item.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}