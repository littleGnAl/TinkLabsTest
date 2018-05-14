package com.littlegnal.tinklabstest.ui.home

import com.littlegnal.tinklabstest.common.mvi.LceStatus
import com.littlegnal.tinklabstest.common.mvi.MviResult
import com.littlegnal.tinklabstest.ui.home.adapter.HomeAdapterItem

/**
 * @author littlegnal
 * @date 2018/5/12.
 */
sealed class HomeResult : MviResult {

  data class InitialResult(
      val lceStatus: LceStatus,
      val error: Throwable?,
      val adapterList: List<HomeAdapterItem>,
      val isNoMoreData: Boolean
  ) : HomeResult() {
    companion object {
      fun success(adapterList: List<HomeAdapterItem>, isNoMoreData: Boolean) =
          InitialResult(LceStatus.SUCCESS, null, adapterList, isNoMoreData)

      fun failure(error: Throwable) =
          InitialResult(LceStatus.FAILURE, error, listOf(), false)

      fun inFlight() =
          InitialResult(LceStatus.IN_FLIGHT, null, listOf(), false)
    }
  }

  class LoadNextPageResult(
      val lceStatus: LceStatus,
      val error: Throwable?,
      val adapterList: List<HomeAdapterItem>,
      val isNoMoreData: Boolean
  ) : HomeResult() {
    companion object {
      fun success(adapterList: List<HomeAdapterItem>, isNoMoreData: Boolean) =
          LoadNextPageResult(LceStatus.SUCCESS, null, adapterList, isNoMoreData)

      fun failure(error: Throwable?) =
          LoadNextPageResult(LceStatus.FAILURE, error, listOf(), false)

      fun inFlight() =
          LoadNextPageResult(LceStatus.IN_FLIGHT, null, listOf(), false)
    }
  }
}