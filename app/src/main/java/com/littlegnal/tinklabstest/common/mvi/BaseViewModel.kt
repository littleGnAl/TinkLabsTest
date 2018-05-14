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

package com.littlegnal.tinklabstest.common.mvi

import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * 抽象[MviViewModel]，实现[MviIntent]与[MviViewState]的连接
 */
abstract class BaseViewModel<I : MviIntent, S : MviViewState> : ViewModel(), MviViewModel<I, S> {

  private val intentsSubject = PublishSubject.create<I>()
  private val statesObservable: Observable<S> by lazy { compose(intentsSubject) }

  override fun processIntents(intents: Observable<I>) {
    intents.subscribe(intentsSubject)
  }

  override fun states(): Observable<S> = statesObservable

  abstract fun compose(intentsSubject: PublishSubject<I>): Observable<S>
}