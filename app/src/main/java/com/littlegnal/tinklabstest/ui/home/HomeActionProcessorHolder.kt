package com.littlegnal.tinklabstest.ui.home

import com.littlegnal.tinklabstest.common.mvi.LceStatus.FAILURE
import com.littlegnal.tinklabstest.common.mvi.LceStatus.IN_FLIGHT
import com.littlegnal.tinklabstest.common.mvi.LceStatus.SUCCESS
import com.littlegnal.tinklabstest.common.mvi.MviAction
import com.littlegnal.tinklabstest.common.mvi.MviResult
import com.littlegnal.tinklabstest.common.schedulers.BaseSchedulerProvider
import com.littlegnal.tinklabstest.ui.home.adapter.HomeAdapterItem
import com.littlegnal.tinklabstest.ui.home.adapter.HomeAdapterItemNomal
import com.littlegnal.tinklabstest.ui.home.adapter.HomeAdapterItemSingleImage
import com.littlegnal.tinklabstest.ui.home.data.HomeBackendApi
import com.littlegnal.tinklabstest.ui.home.data.PageItem
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import timber.log.Timber

/**
 * 分页一页数据大小
 */
const val ONE_PAGE_SIZE = 5
const val NORMAL_PAGE_ITEM = 1000

/**
 * 处理[HomeAction]业务逻辑，然后返回[HomeResult]
 *
 * @see actionAndReducer
 *
 * @author littlegnal
 * @date 2018/5/13.
 */
class HomeActionProcessorHolder constructor(
  private val homeBackendApi: HomeBackendApi,
  private val schedulerProvider: BaseSchedulerProvider
) {

  private val initialProcessor:
      ObservableTransformer<HomeAction.InitialAction, HomeResult.InitialResult> =
    ObservableTransformer { actions ->
      actions.flatMap { action ->
        homeBackendApi.getPage(action.category, 1)
            .map {
              val adapterList = generateAdapterList(it)
              HomeResult.InitialResult.success(adapterList, adapterList.size < ONE_PAGE_SIZE)
            }
            .onErrorReturn {
              Timber.e(it)
              HomeResult.InitialResult.failure(it)
            }
            .subscribeOn(schedulerProvider.io())
            .startWith(HomeResult.InitialResult.inFlight())
      }
    }

  private val loadNextPageProcessor:
      ObservableTransformer<HomeAction.LoadNextPageAction, HomeResult.LoadNextPageResult> =
    ObservableTransformer { actions ->
      actions.flatMap { action ->
        val page = if (action.offset % ONE_PAGE_SIZE == 0) {
          action.offset / ONE_PAGE_SIZE + 1
        } else {
          action.offset / ONE_PAGE_SIZE + 2
        }
        homeBackendApi.getPage(action.category, page)
            .map {
              val adapterList = generateAdapterList(it)
              HomeResult.LoadNextPageResult.success(adapterList, adapterList.size < ONE_PAGE_SIZE)
            }
            .onErrorReturn {
              Timber.e(it)
              HomeResult.LoadNextPageResult.failure(it)
            }
            .subscribeOn(schedulerProvider.io())
            .startWith(HomeResult.LoadNextPageResult.inFlight())
      }
    }

  private fun generateAdapterList(pageItemList: List<PageItem>): List<HomeAdapterItem> {
    val adapterList = mutableListOf<HomeAdapterItem>()
    pageItemList.mapTo(adapterList) { pageItem ->
      if (pageItem.type == NORMAL_PAGE_ITEM) {
        HomeAdapterItemNomal(pageItem.id, pageItem.imgUrl, pageItem.title, pageItem.description)
      } else {
        HomeAdapterItemSingleImage(pageItem.id, pageItem.imgUrl)
      }
    }

    return adapterList
  }

  private val reducer = BiFunction<HomeViewState, HomeResult, HomeViewState> { preState, result ->
    when (result) {
      is HomeResult.InitialResult -> {
        when (result.lceStatus) {
          SUCCESS -> {
            preState.copy(
                error = null,
                adapterList = result.adapterList,
                isNoMoreData = result.isNoMoreData,
                isLoadingNextPage = false
            )
          }
          FAILURE -> {
            preState.copy(
                error = result.error,
                isLoadingNextPage = false
            )
          }
          IN_FLIGHT -> {
            preState.copy(error = null, isLoadingNextPage = true)
          }
        }
      }
      is HomeResult.LoadNextPageResult -> {
        when (result.lceStatus) {
          SUCCESS -> {
            val newAdapterList = preState.adapterList.toMutableList()
                .apply {
                  addAll(result.adapterList)
                }
            preState.copy(
                error = null,
                adapterList = newAdapterList,
                isNoMoreData = result.isNoMoreData,
                isLoadingNextPage = false
            )
          }
          FAILURE -> {
            preState.copy(
                error = result.error,
                isLoadingNextPage = false
            )
          }
          IN_FLIGHT -> {
            preState.copy(
                error = null,
                isLoadingNextPage = true
            )
          }
        }
      }
    }
  }

  /**
   * 拆分[Observable<MviAction>]并且为不同的[MviAction]提供相应的processor，processor用于处理业务逻辑，
   * 同时把[MviAction]转换为[MviResult]，最终通过[Observable.merge]合并回一个流
   *
   * 这里为了让Reducer在[BaseSchedulerProvider.io]线程中执行，在processor合并之后就进行[Observable.scan]
   * 处理
   *
   * 为了防止遗漏[MviAction]未处理，在流的最后合并一个错误检测，方便维护
   */
  val actionAndReducer = ObservableTransformer<HomeAction, HomeViewState> { actions ->
    actions.publish { shared ->
      Observable.merge(
          shared.ofType(HomeAction.InitialAction::class.java).compose(initialProcessor),
          shared.ofType(HomeAction.LoadNextPageAction::class.java).compose(loadNextPageProcessor)
      )
      .mergeWith(shared.filter {
          it !is HomeAction.InitialAction &&
              it !is HomeAction.LoadNextPageAction
        }
        .flatMap {
          Observable.error<HomeResult>(
              IllegalArgumentException("Unknown Action type: $it")
          )
        })
      .scan(HomeViewState.idle(), reducer)
      .observeOn(schedulerProvider.ui())

    }
  }

}