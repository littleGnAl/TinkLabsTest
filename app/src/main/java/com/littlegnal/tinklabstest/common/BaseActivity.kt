/*
 * Copyright (C) 2018 littlegnal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.littlegnal.tinklabstest.common

import android.os.Build
import android.support.annotation.ColorInt
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import com.littlegnal.tinklabstest.R
import com.littlegnal.tinklabstest.common.extensions.colorRes

/**
 * @author littlegnal
 * @date 2018/5/12.
 */
open class BaseActivity : AppCompatActivity() {
  protected lateinit var toolbar: Toolbar

  override fun setContentView(layoutResID: Int) {
    super.setContentView(layoutResID)
    initToolbar()
  }

  override fun setContentView(view: View?) {
    super.setContentView(view)
    initToolbar()
  }

  override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
    super.setContentView(view, params)
    initToolbar()
  }

  private fun initToolbar() {
    toolbar = findViewById(R.id.base_toolbar)
    setSupportActionBar(toolbar)
    setImmersionStatusBar()
  }

  private fun setImmersionStatusBar() {
    if (Build.VERSION.SDK_INT in 19..20) {
      setFlagTranslucentStatus(window)
    } else if (Build.VERSION.SDK_INT >= 21) {
      setStatusBarColor(window, colorRes(android.R.color.transparent))
    }
  }

  private fun setStatusBarColor(window: Window?, @ColorInt color: Int): Boolean {
    if (window == null) return false
    if (Build.VERSION.SDK_INT >= 21) {
      setViewFullScreen(window.decorView)
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
      window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
      window.statusBarColor = color

      return true
    }

    return false
  }

  /**
   * 给`view`设置全屏标志 [View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN]和
   * [View.SYSTEM_UI_FLAG_LAYOUT_STABLE]
   *
   * See: [View.setSystemUiVisibility]
   *
   * @param view [View]
   */
  private fun setViewFullScreen(view: View): Boolean {
    if (Build.VERSION.SDK_INT >= 19) {
      view.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
      view.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
      return true
    }

    return false
  }

  /**
   * Android 4.4以上设置透明状态栏
   * @param window [Window]
   * @return 设置成功返回 `true`
   */
  private fun setFlagTranslucentStatus(window: Window): Boolean {
    if (Build.VERSION.SDK_INT >= 19) {
      window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
    return Build.VERSION.SDK_INT >= 19
  }
}