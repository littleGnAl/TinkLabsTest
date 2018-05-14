package com.littlegnal.tinklabstest.ui.home

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import com.littlegnal.tinklabstest.R
import com.littlegnal.tinklabstest.common.LazyFragment
import com.littlegnal.tinklabstest.common.MarginDecoration
import com.littlegnal.tinklabstest.common.extensions.dip
import com.littlegnal.tinklabstest.common.extensions.plusAssign
import com.littlegnal.tinklabstest.common.extensions.toast
import com.littlegnal.tinklabstest.di.Injectable
import com.littlegnal.tinklabstest.ui.home.adapter.HomeController
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_home.rv_list
import javax.inject.Inject

/**
 * @author littlegnal
 * @date 2018/5/12.
 */
class HomeFragment : LazyFragment(), Injectable {

  companion object {

    const val KEY_CATEGORY = "KEY_CATEGORY"

    fun newInstance(category: String): Fragment {
      return HomeFragment().apply {
        arguments = Bundle().apply {
          putString(KEY_CATEGORY, category)
        }
      }
    }
  }

  @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

  private lateinit var homeViewModel: HomeViewModel

  @Inject lateinit var disposables: CompositeDisposable

  private lateinit var category: String

  private val homeController: HomeController by lazy {
    HomeController()
  }

  private val layoutManager: LinearLayoutManager by lazy {
    LinearLayoutManager(context)
  }

  private val loadDataPublisher: PublishSubject<Boolean> by lazy {
    PublishSubject.create<Boolean>()
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    category = arguments?.getString(KEY_CATEGORY, "")!!
    return inflater.inflate(R.layout.fragment_home, container, false)
  }

  override fun loadData() {
    loadDataPublisher.onNext(true)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    rv_list.layoutManager = layoutManager
    rv_list.adapter = homeController.adapter
    rv_list.addItemDecoration(MarginDecoration(
        top = context?.dip(10)!!,
        epoxyControllerAdapter = homeController.adapter) { position, _ -> position != 0 })

    bind()
    super.onViewCreated(view, savedInstanceState)
  }

  private fun initialIntent(): Observable<HomeIntent> =
    loadDataPublisher.map<HomeIntent> { HomeIntent.InitialIntent(category) }
      .filter { category.isNotEmpty() }

  private fun loadNextPageIntent(): Observable<HomeIntent> =
    RxRecyclerView.scrollStateChanges(rv_list)
        .filter { !homeController.isNoMoreData }
        .filter { !homeController.isLoadingNextPage }
        .filter { it == RecyclerView.SCROLL_STATE_IDLE }
        .filter { layoutManager.findLastCompletelyVisibleItemPosition() ==
            homeController.adapter.itemCount - 1 }
        .map {
          HomeIntent.LoadNextPageIntent(category, homeController.adapter.itemCount)
        }

  private fun intents(): Observable<HomeIntent> =
      Observable.merge(initialIntent(), loadNextPageIntent())

  private fun bind() {
    homeViewModel = ViewModelProviders.of(this, viewModelFactory)
        .get(HomeViewModel::class.java)
    disposables += homeViewModel.states().subscribe(this::render)
    homeViewModel.processIntents(intents())
  }

  private fun render(state: HomeViewState) {
    if (state.error != null) {
      activity?.toast(state.error.message.toString())
    }

    homeController.isNoMoreData = state.isNoMoreData
    homeController.setData(state.adapterList, state.isLoadingNextPage)
  }

  override fun onDestroy() {
    super.onDestroy()
    disposables.dispose()
  }

}