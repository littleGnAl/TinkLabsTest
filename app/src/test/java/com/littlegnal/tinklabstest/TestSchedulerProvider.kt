package com.littlegnal.tinklabstest

import com.littlegnal.tinklabstest.common.schedulers.BaseSchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

/**
 * 测试[BaseSchedulerProvider]，让`ui()`方法返回[Schedulers.trampoline]，方便测试
 *
 * @author littlegnal
 */
class TestSchedulerProvider : BaseSchedulerProvider {
  override fun computation(): Scheduler = Schedulers.trampoline()

  override fun io(): Scheduler = Schedulers.trampoline()

  override fun ui(): Scheduler = Schedulers.trampoline()
}