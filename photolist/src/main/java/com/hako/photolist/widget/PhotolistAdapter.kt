package com.hako.photolist.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hako.base.extensions.autoNotify
import com.hako.photolist.R
import com.hako.photolist.model.Photo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_photo_card.view.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.properties.Delegates

class PhotolistAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items by Delegates.observable(emptyList<Photo>()) { _, oldList, newList ->
        autoNotify(oldList, newList) { old, new -> old.id == new.id }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        PhotoViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_photo_card, parent, false)
        )

    fun getItem(position: Int) = items[position]

    fun addAll(list: List<Photo>) {
        items = list
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(viewholder: RecyclerView.ViewHolder, position: Int) =
        when (viewholder) {
            is PhotoViewHolder -> viewholder.bind(items[position])
            else -> throw NoWhenBranchMatchedException("Undefined viewholder")
        }
}

class PhotoViewHolder(private val view: View) :
    RecyclerView.ViewHolder(view), KoinComponent {

    private val picasso: Picasso by inject()

    fun bind(photo: Photo) = with(view) {
        picasso.load(photo.photoUrl)
            .placeholder(R.drawable.img_photo_placeholder)
            .fit()
            .into(item_photo_card_photo)
        item_photo_card_title.text = photo.title
    }
}