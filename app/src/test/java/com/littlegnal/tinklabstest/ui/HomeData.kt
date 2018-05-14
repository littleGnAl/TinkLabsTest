package com.littlegnal.tinklabstest.ui

import com.littlegnal.tinklabstest.ui.home.NORMAL_PAGE_ITEM
import com.littlegnal.tinklabstest.ui.home.adapter.HomeAdapterItem
import com.littlegnal.tinklabstest.ui.home.adapter.HomeAdapterItemNomal
import com.littlegnal.tinklabstest.ui.home.adapter.HomeAdapterItemSingleImage
import com.littlegnal.tinklabstest.ui.home.data.PageItem
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

/**
 * @author littlegnal
 * @date 2018/5/14.
 */
class HomeData {

  companion object {

    const val CATEGORY_CITY = "city"
    const val ONE_PAGE_SIZE = 5

    /**
     * `city`第一页数据
     */
    fun firstCityPageItems(): List<PageItem> {
      val firstPageJson = """
        [
          {
              "id" : 1,
              "type" : 1000,
              "imgUrl" : "1.jpg",
              "title" : "City 1",
              "description" : "Here is City 1!"
          },
          {
              "id" : 2,
              "type" : 1000,
              "imgUrl" : "2.jpg",
              "title" : "City 2",
              "description" : "Here is City 2!"
          },
          {
              "id" : 3,
              "type" : 1001,
              "imgUrl" : "3.jpg"
          },
          {
              "id" : 4,
              "type" : 1000,
              "imgUrl" : "4.jpg",
              "title" : "City 4",
              "description" : "Here is City 4!"
          },
          {
              "id" : 5,
              "type" : 1000,
              "imgUrl" : "5.jpg",
              "title" : "City 5",
              "description" : "Here is City 5!"
          }
      ]
      """.trimIndent()
      return toPageItemList(firstPageJson)
    }

    /**
     * `city`第二页数据
     */
    fun secondCityPageItems(): List<PageItem> {
      val secondPageJson = """
        [
          {
              "id" : 6,
              "type" : 1000,
              "imgUrl" : "6.jpg",
              "title" : "City 6",
              "description" : "Here is City 1!"
          },
          {
              "id" : 7,
              "type" : 1000,
              "imgUrl" : "7.jpg",
              "title" : "City 7",
              "description" : "Here is City 7!"
          },
          {
              "id" : 8,
              "type" : 1001,
              "imgUrl" : "8.jpg"
          },
          {
              "id" : 9,
              "type" : 1000,
              "imgUrl" : "9.jpg",
              "title" : "City 9",
              "description" : "Here is City 9!"
          },
          {
              "id" : 10,
              "type" : 1000,
              "imgUrl" : "10.jpg",
              "title" : "City 10",
              "description" : "Here is City 10!"
          }
      ]
      """.trimIndent()

      return toPageItemList(secondPageJson)
    }

    private fun toPageItemList(json: String): List<PageItem> {
      val moshi = Moshi.Builder().build()
      val type = Types.newParameterizedType(List::class.java, PageItem::class.java)
      val adapter = moshi.adapter<List<PageItem>>(type)
      return adapter.fromJson(json)!!
    }

    private fun generateAdapterList(pageItemList: List<PageItem>): List<HomeAdapterItem> {
      val adapterList = mutableListOf<HomeAdapterItem>()
      pageItemList.mapTo(adapterList) { pageItem ->
        if (pageItem.type == NORMAL_PAGE_ITEM) {
          HomeAdapterItemNomal(pageItem.id, pageItem.imgUrl, pageItem.title, pageItem.description)
        } else {
          HomeAdapterItemSingleImage(pageItem.id, pageItem.imgUrl)
        }
      }

      return adapterList
    }

    /**
     * `city`第一页adapter list
     */
    fun firstPageAdapterList(): List<HomeAdapterItem> = generateAdapterList(firstCityPageItems())

    /**
     * `city`第二页adapter list
     */
    fun secondPageAdapterList(): List<HomeAdapterItem> = generateAdapterList(secondCityPageItems())

    /**
     * 空adapter list
     */
    fun initialAdapterList() = listOf<HomeAdapterItem>()

    /**
     * 空page item list
     */
    fun emptyPageItemList() = listOf<PageItem>()
  }
}