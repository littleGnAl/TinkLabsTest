package com.littlegnal.tinklabstest.ui.home.data

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author littlegnal
 * @date 2018/5/13.
 */
interface HomeBackendApi {

  @GET("{category}/{pagination}.json")
  fun getPage(
    @Path("category") category: String,
    @Path("pagination") pagination: Int
  ): Observable<List<PageItem>>

}