package com.littlegnal.tinklabstest.common

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View

/**
 * @author littlegnal
 * @date 2018/5/14.
 */
abstract class LazyFragment : Fragment() {

  private var isFirstLoad: Boolean = true
  private var isPrepared: Boolean = false

//  override fun onCreateView(
//    inflater: LayoutInflater,
//    container: ViewGroup?,
//    savedInstanceState: Bundle?
//  ): View? {
//    val view = createView(inflater, container, savedInstanceState)
//    isFirstLoad = true
//    isPrepared = true
//    lazyLoad()
//    return view
//  }
//
//  abstract fun createView(
//    inflater: LayoutInflater,
//    container: ViewGroup?,
//    savedInstanceState: Bundle?
//  ): View

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    isFirstLoad = true
    isPrepared = true
    lazyLoad()
  }

//  abstract fun viewCreated()

  abstract fun loadData()

  private fun lazyLoad() {
    if (!userVisibleHint || !isPrepared || !isFirstLoad) {
      return
    }

    isFirstLoad = false
    loadData()
  }

  override fun setUserVisibleHint(isVisibleToUser: Boolean) {
    super.setUserVisibleHint(isVisibleToUser)
    if (isVisibleToUser) {
      lazyLoad()
    }
  }
}