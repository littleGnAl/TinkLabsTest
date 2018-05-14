package com.littlegnal.tinklabstest.ui.home

import com.littlegnal.tinklabstest.common.mvi.MviIntent

/**
 * [HomeFragment]用户意图
 * * 初始化
 * * 上拉自动加载下一页
 *
 * @author littlegnal
 * @date 2018/5/12.
 */
sealed class HomeIntent : MviIntent {

  /**
   * 初始化**Intent**
   * * 请求第一页数据
   */
  data class InitialIntent(val category: String) : HomeIntent()

  /**
   * 加载下一页数据**Intent**
   */
  data class LoadNextPageIntent(val category: String, val offset: Int) : HomeIntent()
}