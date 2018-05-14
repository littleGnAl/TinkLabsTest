package com.littlegnal.tinklabstest.di

import android.arch.lifecycle.ViewModelProvider
import com.littlegnal.tinklabstest.common.ViewModelFactory
import dagger.Binds
import dagger.Module

/**
 * @author littlegnal
 * @date 2017/12/21
 */
@Module
abstract class ViewModelModule {

  @Binds
  abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

}