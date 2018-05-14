package com.littlegnal.tinklabstest.ui.home

import com.littlegnal.tinklabstest.common.mvi.BaseViewModel
import com.littlegnal.tinklabstest.common.mvi.MviAction
import com.littlegnal.tinklabstest.common.mvi.MviIntent
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

/**
 * @author littlegnal
 * @date 2018/5/13.
 */
class HomeViewModel @Inject constructor(
  private val homeActionProcessorHolder: HomeActionProcessorHolder
) : BaseViewModel<HomeIntent, HomeViewState>() {

  override fun compose(intentsSubject: PublishSubject<HomeIntent>): Observable<HomeViewState> {
    return intentsSubject
        .compose(intentFilter)
        .map(this::actionFromIntent)
        .compose(homeActionProcessorHolder.actionAndReducer)
        .replay(1)
        .autoConnect(0)
  }

  /**
   * 只取第一个InitialIntent和其他类型的所有意图，避免旋转屏幕的时候数据被重新加载
   */
  private val intentFilter: ObservableTransformer<HomeIntent, HomeIntent> =
    ObservableTransformer {
      intents -> intents.publish {
      shared -> Observable.merge(
        shared.ofType(HomeIntent.InitialIntent::class.java).take(1),
        shared.filter { it !is HomeIntent.InitialIntent })
      }
    }

  /**
   * 把[MviIntent]转换为[MviAction]
   */
  private fun actionFromIntent(homeIntent: HomeIntent): HomeAction {
    return when(homeIntent) {
      is HomeIntent.InitialIntent -> HomeAction.InitialAction(homeIntent.category)
      is HomeIntent.LoadNextPageIntent -> {
        HomeAction.LoadNextPageAction(homeIntent.category, homeIntent.offset)
      }
    }
  }
}