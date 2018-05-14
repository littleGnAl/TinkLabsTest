package com.littlegnal.tinklabstest

import com.facebook.stetho.Stetho
import com.littlegnal.tinklabstest.di.AppComponent
import com.littlegnal.tinklabstest.di.DaggerDebugComponent
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber

/**
 * **Debug**版本的[android.app.Application]，用于添加**Debug**配置
 *
 * @author littlegnal
 * @date 2017/12/21
 */
class DebugApp : App() {

  override fun onCreate() {
    // This process is dedicated to LeakCanary for heap analysis.
    // You should not init your app in this process.
    if (LeakCanary.isInAnalyzerProcess(this)) return
    super.onCreate()
    LeakCanary.install(this)
    Stetho.initializeWithDefaults(this)
    Timber.plant(Timber.DebugTree())
  }

  override fun provideAppComponentBuilder(): AppComponent.Builder = DaggerDebugComponent.builder()
}