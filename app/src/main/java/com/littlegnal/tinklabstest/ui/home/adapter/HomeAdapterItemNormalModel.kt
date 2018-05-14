package com.littlegnal.tinklabstest.ui.home.adapter

import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.view.View
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.littlegnal.tinklabstest.R
import com.littlegnal.tinklabstest.common.extensions.dip
import com.littlegnal.tinklabstest.ui.home.adapter.HomeAdapterItemNormalModel.NormalHolder
import com.squareup.picasso.Picasso

/**
 * @author littlegnal
 * @date 2018/5/13.
 */

@EpoxyModelClass(layout = R.layout.adapter_home_item_normal)
abstract class HomeAdapterItemNormalModel : EpoxyModelWithHolder<NormalHolder>() {

  @EpoxyAttribute var normalItem: HomeAdapterItemNomal? = null

  override fun bind(holder: NormalHolder?) {
    holder?.apply {
      tvTitle.text = normalItem?.title
      tvDescription.text = normalItem?.description

      normalItem?.imgUrl?.apply {
        if (isNotEmpty()) {
          Picasso.with(img.context)
              .load(this)
              .placeholder(R.drawable.img_place_holder)
              .resize(img.context.dip(150), img.context.dip(100))
              .centerCrop()
              .into(img)
        }
      }

    }

  }

  class NormalHolder : EpoxyHolder() {

    lateinit var img: AppCompatImageView
    lateinit var tvTitle: AppCompatTextView
    lateinit var tvDescription: AppCompatTextView

    override fun bindView(itemView: View?) {
      itemView?.apply {
        img = findViewById(R.id.img_adapter_home_item_normal)
        tvTitle = findViewById(R.id.tv_adapter_home_item_normal_title)
        tvDescription = findViewById(R.id.tv_adapter_home_item_normal_description)
      }
    }

  }
}