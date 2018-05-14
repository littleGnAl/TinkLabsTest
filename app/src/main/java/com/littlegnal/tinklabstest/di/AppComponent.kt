package com.littlegnal.tinklabstest.di

import android.app.Application
import com.littlegnal.tinklabstest.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * @author littlegnal
 * @date 2018/5/12.
 */

@Singleton
@Component(modules = [
  AndroidInjectionModule::class,
  AppModule::class
])
interface AppComponent {

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun application(application: Application): Builder

    fun build(): AppComponent
  }

  fun inject(app: App)
}