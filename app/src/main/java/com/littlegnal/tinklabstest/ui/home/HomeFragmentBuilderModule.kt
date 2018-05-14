package com.littlegnal.tinklabstest.ui.home

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class HomeFragmentBuilderModule {

  @ContributesAndroidInjector
  abstract fun contributeHomeFragment(): HomeFragment

}