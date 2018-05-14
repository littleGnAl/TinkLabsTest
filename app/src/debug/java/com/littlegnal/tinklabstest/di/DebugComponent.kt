package com.littlegnal.tinklabstest.di

import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * @author littlegnal
 * @date 2017/12/21
 */

@Singleton
@Component(modules = [AndroidInjectionModule::class, DebugAppModule::class])
interface DebugComponent : AppComponent {

  @Component.Builder
  interface Builder : AppComponent.Builder

}