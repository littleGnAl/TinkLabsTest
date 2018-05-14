package com.littlegnal.tinklabstest.ui.home

import android.arch.lifecycle.ViewModel
import com.littlegnal.tinklabstest.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * @author littlegnal
 * @date 2018/5/13.
 */
@Module
abstract class HomeBuilderModule {

  @ContributesAndroidInjector(modules = [HomeFragmentBuilderModule::class])
  abstract fun contributeHomeActivity(): HomeActivity

  @Binds
  @IntoMap
  @ViewModelKey(HomeViewModel::class)
  abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel
}