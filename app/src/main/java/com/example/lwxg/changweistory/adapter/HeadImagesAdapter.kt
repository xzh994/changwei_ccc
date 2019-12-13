package com.example.lwxg.changweistory.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.lwxg.changweistory.R
import com.example.lwxg.changweistory.data.SomeDatas
import com.example.lwxg.changweistory.util.LocalCacheUtils

class HeadImagesAdapter(val context: Context, val list: IntArray) : RecyclerView.Adapter<HeadImagesAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onClick(position: Int, view: View)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_head_images, null, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.run {
            if (LocalCacheUtils.getCache(SomeDatas.ARRAY_HEAD_IMAGES_URL[position]) == null) {
                LocalCacheUtils.setCache(SomeDatas.ARRAY_HEAD_IMAGES_URL[position], BitmapFactory.decodeResource(context.resources, list[position]))
            }
            itemHeadImage.setImageBitmap(LocalCacheUtils.getCache(SomeDatas.ARRAY_HEAD_IMAGES_URL[position]))
            itemHeadImage.setOnClickListener { listener?.onClick(position, itemHeadImage) }
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemHeadImage: ImageView = itemView.findViewById(R.id.item_head_image)
    }
}