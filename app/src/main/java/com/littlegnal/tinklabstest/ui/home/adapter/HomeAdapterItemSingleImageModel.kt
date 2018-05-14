package com.littlegnal.tinklabstest.ui.home.adapter

import android.support.v7.widget.AppCompatImageView
import android.view.View
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.littlegnal.tinklabstest.R
import com.littlegnal.tinklabstest.common.extensions.dip
import com.littlegnal.tinklabstest.common.extensions.screenSize
import com.littlegnal.tinklabstest.ui.home.adapter.HomeAdapterItemSingleImageModel.SingleImageHolder
import com.squareup.picasso.Picasso

/**
 * @author littlegnal
 * @date 2018/5/13.
 */

@EpoxyModelClass(layout = R.layout.adapter_home_item_single_image)
abstract class HomeAdapterItemSingleImageModel : EpoxyModelWithHolder<SingleImageHolder>() {

  @EpoxyAttribute var singleImageItem: HomeAdapterItemSingleImage? = null

  override fun bind(holder: SingleImageHolder?) {
    holder?.apply {
      singleImageItem?.imgUrl?.apply {
        if (isNotEmpty()) {
          val context = img.context
          Picasso.with(context)
              .load(this)
              .placeholder(R.drawable.img_place_holder)
              .resize(context.screenSize().x, context.dip(250))
              .centerCrop()
              .into(img)
        }
      }
    }
  }

  class SingleImageHolder : EpoxyHolder() {

    lateinit var img: AppCompatImageView

    override fun bindView(itemView: View?) {
      itemView?.apply {
        img = findViewById(R.id.img_adapter_home_item_single_image)
      }
    }
  }
}