package com.littlegnal.tinklabstest.di

import android.app.Application
import android.content.Context
import com.jakewharton.picasso.OkHttp3Downloader
import com.littlegnal.tinklabstest.common.schedulers.BaseSchedulerProvider
import com.littlegnal.tinklabstest.common.schedulers.SchedulerProvider
import com.littlegnal.tinklabstest.ui.home.HomeBuilderModule
import com.littlegnal.tinklabstest.ui.home.HomeModule
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import javax.inject.Singleton

/**
 * @author littlegnal
 * @date 2018/5/12.
 */

const val BASE_URL = "https://raw.githubusercontent.com/littleGnAl/TinkLabsTest/master/server/"

@Module(includes = [ViewModelModule::class, HomeBuilderModule::class, HomeModule::class])
open class AppModule {

  @Singleton
  @Provides
  fun provideApplicationContext(application: Application): Context = application.applicationContext

  open fun createOkHttpBuilder(applicationContext: Context): OkHttpClient.Builder {
    val cache = Cache(
        File(applicationContext.cacheDir,
            "tinklabstest"),
        (10 * 1024 * 1024).toLong())
    return OkHttpClient.Builder()
        .cache(cache)

  }

  @Singleton
  @Provides
  fun provideOkHttpClient(applicationContext: Context): OkHttpClient =
    createOkHttpBuilder(applicationContext).build()

  @Singleton
  @Provides
  fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create())
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .build()
  }

  @Provides
  fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()

  @Singleton
  @Provides
  fun provideSchedulerProvider(): BaseSchedulerProvider = SchedulerProvider()

  @Singleton
  @Provides
  fun providePicasso(applicationContext: Context, okHttpClient: OkHttpClient): Picasso =
    Picasso.Builder(applicationContext)
        .downloader(OkHttp3Downloader(okHttpClient))
        .build()
}