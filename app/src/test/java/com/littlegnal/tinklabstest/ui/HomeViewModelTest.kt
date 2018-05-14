package com.littlegnal.tinklabstest.ui

import com.littlegnal.tinklabstest.TestSchedulerProvider
import com.littlegnal.tinklabstest.ui.home.HomeActionProcessorHolder
import com.littlegnal.tinklabstest.ui.home.HomeIntent
import com.littlegnal.tinklabstest.ui.home.HomeViewModel
import com.littlegnal.tinklabstest.ui.home.HomeViewState
import com.littlegnal.tinklabstest.ui.home.adapter.HomeAdapterItem
import com.littlegnal.tinklabstest.ui.home.data.HomeBackendApi
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

/**
 * @author littlegnal
 * @date 2018/5/14.
 */
class HomeViewModelTest {

  @Mock
  private lateinit var homeBackendApi: HomeBackendApi

  private lateinit var homeViewModel: HomeViewModel

  private lateinit var testObserver: TestObserver<HomeViewState>

  @Before
  fun setUp() {
    MockitoAnnotations.initMocks(this)

    homeViewModel = HomeViewModel(
        HomeActionProcessorHolder(homeBackendApi, TestSchedulerProvider()))
    testObserver = homeViewModel.states().test()
  }

  /**
   * 测试[HomeIntent.InitialIntent]成功并返回一页数据（5条）
   */
  @Test
  fun initialIntent_getPageCitySuccess_withOnePageItems() {
    `when`(homeBackendApi.getPage(HomeData.CATEGORY_CITY, 1))
        .thenReturn(Observable.just(HomeData.firstCityPageItems()))

    homeViewModel.processIntents(Observable.just(HomeIntent.InitialIntent(HomeData.CATEGORY_CITY)))

    testGetFirstPageViewState(adapterList = HomeData.firstPageAdapterList())
  }

  /**
   * 测试[HomeIntent.InitialIntent]成功返回少于一页数据（<5条）
   */
  @Test
  fun initialIntent_getPageCitySuccess_lessThanOnePageItems() {
    val firstPageItemList = HomeData.firstCityPageItems().take(3)
    val firstAdapterList = HomeData.firstPageAdapterList().take(3)
    `when`(homeBackendApi.getPage(HomeData.CATEGORY_CITY, 1))
        .thenReturn(Observable.just(firstPageItemList))

    homeViewModel.processIntents(Observable.just(HomeIntent.InitialIntent(HomeData.CATEGORY_CITY)))

    testGetFirstPageViewState(adapterList = firstAdapterList, isNoMoreData = true)
  }

  /**
   * 测试[HomeIntent.InitialIntent]成功但无数据返回
   */
  @Test
  fun initialIntent_getPageCitySuccess_withoutPageItems() {
    `when`(homeBackendApi.getPage(HomeData.CATEGORY_CITY, 1))
        .thenReturn(Observable.just(HomeData.emptyPageItemList()))

    homeViewModel.processIntents(Observable.just(HomeIntent.InitialIntent(HomeData.CATEGORY_CITY)))

    testGetFirstPageViewState(isNoMoreData = true, adapterList = HomeData.initialAdapterList())
  }

  /**
   * 测试[HomeIntent.InitialIntent]请求失败
   */
  @Test
  fun initialIntent_getPageCityFailure() {
    `when`(homeBackendApi.getPage(HomeData.CATEGORY_CITY, 1))
        .thenReturn(Observable.error(Exception()))

    homeViewModel.processIntents(Observable.just(HomeIntent.InitialIntent(HomeData.CATEGORY_CITY)))

    testGetFirstPageViewState(adapterList = HomeData.initialAdapterList(), isError = true)
  }

  /**
   * 测试[HomeIntent.LoadNextPageIntent]成功并返回一页数据（5条）
   */
  @Test
  fun loadNextPageIntent_getPageCitySuccess_withOnePageItems() {
    `when`(homeBackendApi.getPage(HomeData.CATEGORY_CITY, 1))
        .thenReturn(Observable.just(HomeData.firstCityPageItems()))
    `when`(homeBackendApi.getPage(HomeData.CATEGORY_CITY, 2))
        .thenReturn(Observable.just(HomeData.secondCityPageItems()))

    homeViewModel.processIntents(Observable.merge(
        Observable.just(HomeIntent.InitialIntent(HomeData.CATEGORY_CITY)),
        Observable.just(HomeIntent.LoadNextPageIntent(
            HomeData.CATEGORY_CITY,
            HomeData.ONE_PAGE_SIZE))))

    val secondPageAdapterList = HomeData.firstPageAdapterList().toMutableList().apply {
      addAll(HomeData.secondPageAdapterList())
    }
    // 测试第二页
    testGetSecondPageViewState(adapterList = secondPageAdapterList)
  }

  /**
   * 测试[HomeIntent.LoadNextPageIntent]成功返回少于一页数据（<5条）
   */
  @Test
  fun loadNextPageIntent_getPageCitySuccess_lessThanOnePageItems() {
    `when`(homeBackendApi.getPage(HomeData.CATEGORY_CITY, 1))
        .thenReturn(Observable.just(HomeData.firstCityPageItems()))
    `when`(homeBackendApi.getPage(HomeData.CATEGORY_CITY, 2))
        .thenReturn(Observable.just(HomeData.secondCityPageItems().take(3)))

    homeViewModel.processIntents(Observable.merge(
        Observable.just(HomeIntent.InitialIntent(HomeData.CATEGORY_CITY)),
        Observable.just(HomeIntent.LoadNextPageIntent(
            HomeData.CATEGORY_CITY,
            HomeData.ONE_PAGE_SIZE))))

    val secondPageAdapterList = HomeData.firstPageAdapterList().toMutableList().apply {
      addAll(HomeData.secondPageAdapterList().take(3))
    }
    // 测试第二页
    testGetSecondPageViewState(
        adapterList = secondPageAdapterList,
        isNoMoreData = true)
  }

  /**
   * 测试[HomeIntent.LoadNextPageIntent]成功但无数据返回
   */
  @Test
  fun loadNextPageIntent_getPageCitySuccess_withoutPageItems() {
    `when`(homeBackendApi.getPage(HomeData.CATEGORY_CITY, 1))
        .thenReturn(Observable.just(HomeData.firstCityPageItems()))
    `when`(homeBackendApi.getPage(HomeData.CATEGORY_CITY, 2))
        .thenReturn(Observable.just(HomeData.emptyPageItemList()))

    homeViewModel.processIntents(Observable.merge(
        Observable.just(HomeIntent.InitialIntent(HomeData.CATEGORY_CITY)),
        Observable.just(HomeIntent.LoadNextPageIntent(
            HomeData.CATEGORY_CITY,
            HomeData.ONE_PAGE_SIZE))))

    // 测试第二页
    testGetSecondPageViewState(
        adapterList = HomeData.firstPageAdapterList(),
        isNoMoreData = true)
  }

  /**
   * 测试[HomeIntent.LoadNextPageIntent]请求失败
   */
  @Test
  fun loadNextPageIntent_getPageCityFailure() {
    `when`(homeBackendApi.getPage(HomeData.CATEGORY_CITY, 1))
        .thenReturn(Observable.just(HomeData.firstCityPageItems()))
    `when`(homeBackendApi.getPage(HomeData.CATEGORY_CITY, 2))
        .thenReturn(Observable.error(Exception()))

    homeViewModel.processIntents(Observable.merge(
        Observable.just(HomeIntent.InitialIntent(HomeData.CATEGORY_CITY)),
        Observable.just(HomeIntent.LoadNextPageIntent(
            HomeData.CATEGORY_CITY,
            HomeData.ONE_PAGE_SIZE))))

    // 测试第二页
    testGetSecondPageViewState(
        isError = true,
        adapterList = HomeData.firstPageAdapterList())
  }

  private fun testGetFirstPageViewState(
    isError: Boolean = false,
    adapterList: List<HomeAdapterItem>,
    isLoading: Boolean = false,
    isNoMoreData: Boolean = false) {
    // Loading
    testObserver.assertValueAt(1) {
      it.error == null
          && it.adapterList == HomeData.initialAdapterList()
          && !it.isNoMoreData
          && it.isLoadingNextPage
    }

    // Success
    testObserver.assertValueAt(2) {
      val error = if (isError) it.error != null else it.error == null
      error
          && it.adapterList == adapterList
          && it.isLoadingNextPage == isLoading
          && it.isNoMoreData == isNoMoreData
    }
  }

  private fun testGetSecondPageViewState(
    isError: Boolean = false,
    adapterList: List<HomeAdapterItem>,
    isLoading: Boolean = false,
    isNoMoreData: Boolean = false) {
    // 测试加载第一页
    testGetFirstPageViewState(adapterList = HomeData.firstPageAdapterList(), isError = false)
    // 加载第二页Loading
    testObserver.assertValueAt(3) {
      it.error == null
      && it.adapterList == HomeData.firstPageAdapterList()
      && it.isLoadingNextPage
      && !it.isNoMoreData
    }
    // 加载第二页Success
    testObserver.assertValueAt(4) {
      val error = if (isError) it.error != null else it.error == null
      error
        && it.adapterList == adapterList
        && it.isLoadingNextPage == isLoading
        && it.isNoMoreData == isNoMoreData
    }
  }



}