package com.littlegnal.tinklabstest.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks
import com.littlegnal.tinklabstest.App
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector

/**
 * @author littlegnal
 * @date 2018/5/12
 */
class AppInjector {
  companion object {
    fun initDI(appComponentBuilder: AppComponent.Builder, app: App) {
      appComponentBuilder.application(app).build().inject(app)
      app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
        override fun onActivityPaused(activity: Activity?) {

        }

        override fun onActivityResumed(activity: Activity?) {
        }

        override fun onActivityStarted(activity: Activity?) {
        }

        override fun onActivityDestroyed(activity: Activity?) {
        }

        override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        }

        override fun onActivityStopped(activity: Activity?) {
        }

        override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
          if (activity is HasSupportFragmentInjector) {
            AndroidInjection.inject(activity)
          }
          if (activity is FragmentActivity) {
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(object :
                FragmentLifecycleCallbacks() {
              override fun onFragmentCreated(
                fm: FragmentManager?,
                f: Fragment?,
                savedInstanceState: Bundle?
              ) {
                if (f is Injectable) {
                  AndroidSupportInjection.inject(f)
                }
              }
            }, true)
          }
        }
      })
    }
  }
}