package com.landvibe.photodiary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_view_content.view.*

class ViewContentRecyclerViewAdapter(
    val items: MutableList<String> = mutableListOf()
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /*onCreateViewHolder : RecyclerView의 행을 표시하는데 사용되는 layout xml을 가져옴*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewContentRecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_view_content, parent, false))
    }

    /*RecyclerView의 행에 보여질 ImageView와 TextView 설정*/
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        bindDefaultView(holder as ViewContentRecyclerViewHolder, position)
    }

    private fun bindDefaultView(holder: ViewContentRecyclerViewHolder, position: Int) {
        if (position < items.size) {
            holder.itemView.item_view_content_text.text = items[position]
        } else {
            holder.itemView.item_view_content_text.text = ""
        }
    }

    override fun getItemCount(): Int {
        return 70
    }
}