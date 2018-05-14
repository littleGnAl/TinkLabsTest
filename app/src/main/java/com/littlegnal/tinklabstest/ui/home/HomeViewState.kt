package com.littlegnal.tinklabstest.ui.home

import com.littlegnal.tinklabstest.common.mvi.MviViewState
import com.littlegnal.tinklabstest.ui.home.adapter.HomeAdapterItem

/**
 * @author littlegnal
 * @date 2018/5/12.
 */
data class HomeViewState(
    val error: Throwable?,
    val adapterList: List<HomeAdapterItem>,
    val isNoMoreData: Boolean,
    val isLoadingNextPage: Boolean
) : MviViewState {

  companion object {
    fun idle() = HomeViewState(
        null,
        listOf(),
        false,
        false)
  }
}