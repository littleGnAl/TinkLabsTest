package com.littlegnal.tinklabstest.ui.home.adapter

import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.Typed2EpoxyController

/**
 * @author littlegnal
 * @date 2018/5/13.
 */
class HomeController : Typed2EpoxyController<List<HomeAdapterItem>, Boolean>() {

  /**
   * 是否正在加载下一页数据
   */
  var isLoadingNextPage: Boolean = false

  /**
   * 是否还能加载更多数据，为`true`则拉到最后一个item时不会触发加载下一页
   */
  var isNoMoreData: Boolean = false

  @AutoModel lateinit var loadingModel: AdapterLoadingModel_

  override fun buildModels(
    adapterList: List<HomeAdapterItem>?,
    isLoading: Boolean?
  ) {
    adapterList?.forEach {
      when(it) {
        is HomeAdapterItemNomal -> {
          homeAdapterItemNormal {
            id(it.id)
            normalItem(it)
          }
        }
        is HomeAdapterItemSingleImage -> {
          homeAdapterItemSingleImage {
            id(it.id)
            singleImageItem(it)
          }
        }
      }
    }

    isLoading?.also {
      loadingModel.addIf(it, this)
    }
  }

  override fun setData(
    data1: List<HomeAdapterItem>?,
    data2: Boolean?
  ) {
    super.setData(data1, data2)
    data2?.apply {
      isLoadingNextPage = this
    }
  }
}