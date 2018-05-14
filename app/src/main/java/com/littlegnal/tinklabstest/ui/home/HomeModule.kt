package com.littlegnal.tinklabstest.ui.home

import com.littlegnal.tinklabstest.common.schedulers.BaseSchedulerProvider
import com.littlegnal.tinklabstest.ui.home.data.HomeBackendApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * @author littlegnal
 * @date 2018/5/13.
 */
@Module
class HomeModule {

  @Provides
  fun provideHomeBackendApi(retrofit: Retrofit): HomeBackendApi
      = retrofit.create(HomeBackendApi::class.java)

  @Provides
  fun provideHomeActionProcessorHolder(
    homeBackendApi: HomeBackendApi,
    schedulerProvider: BaseSchedulerProvider
  ): HomeActionProcessorHolder
      = HomeActionProcessorHolder(homeBackendApi, schedulerProvider)
}