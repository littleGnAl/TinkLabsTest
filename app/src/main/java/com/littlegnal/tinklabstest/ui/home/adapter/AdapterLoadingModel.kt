package com.littlegnal.tinklabstest.ui.home.adapter

import android.view.View
import android.widget.ProgressBar
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.littlegnal.tinklabstest.R

/**
 * @author littlegnal
 * @date 2017/12/24.
 */
@EpoxyModelClass(layout = R.layout.adapter_loading_layout)
abstract class AdapterLoadingModel : EpoxyModelWithHolder<AdapterLoadingModel.LoadingHolder>() {

  override fun bind(holder: LoadingHolder?) {
    super.bind(holder)

    holder?.pbLoading?.isIndeterminate = true
  }

  override fun unbind(holder: LoadingHolder?) {
    super.unbind(holder)

    holder?.pbLoading?.isIndeterminate = false
  }

  class LoadingHolder : EpoxyHolder() {

    lateinit var pbLoading: ProgressBar

    override fun bindView(itemView: View?) {
      itemView?.apply {
        pbLoading = findViewById(R.id.pb_loading)
      }
    }

  }
}