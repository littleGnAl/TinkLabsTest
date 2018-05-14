package com.littlegnal.tinklabstest.ui.home

import com.littlegnal.tinklabstest.common.mvi.MviAction

/**
 * @author littlegnal
 * @date 2018/5/12.
 */
sealed class HomeAction : MviAction {

  data class InitialAction(val category: String) : HomeAction()

  data class LoadNextPageAction(val category: String, val offset: Int) : HomeAction()
}