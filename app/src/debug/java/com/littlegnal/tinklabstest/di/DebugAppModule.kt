package com.littlegnal.tinklabstest.di

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * @author littlegnal
 * @date 2017/12/21
 */
@Module
class DebugAppModule : AppModule() {

  override fun createOkHttpBuilder(applicationContext: Context): OkHttpClient.Builder =
      super.createOkHttpBuilder(applicationContext)
          .addInterceptor(HttpLoggingInterceptor()
              .apply { level = HttpLoggingInterceptor.Level.BODY })
          .addInterceptor(StethoInterceptor())
}