package com.littlegnal.tinklabstest

import android.app.Activity
import android.app.Application
import com.littlegnal.tinklabstest.di.AppComponent
import com.littlegnal.tinklabstest.di.AppInjector
import com.littlegnal.tinklabstest.di.DaggerAppComponent
import com.squareup.picasso.Picasso
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

/**
 * @author littlegnal
 * @date 2018/5/12.
 */
open class App : Application(), HasActivityInjector {

  @Inject lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

  @Inject lateinit var picasso: Picasso

  override fun onCreate() {
    super.onCreate()

    AppInjector.initDI(provideAppComponentBuilder(), this)
    Picasso.setSingletonInstance(picasso)
  }

  open fun provideAppComponentBuilder(): AppComponent.Builder = DaggerAppComponent.builder()

  override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector
}