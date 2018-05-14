package com.littlegnal.tinklabstest.common

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.airbnb.epoxy.EpoxyControllerAdapter
import com.airbnb.epoxy.EpoxyModel

/**
 * @author littlegnal
 */
class MarginDecoration(
    private val left: Int = 0,
    private val top: Int = 0,
    private val right: Int = 0,
    private val bottom: Int = 0,
    private val epoxyControllerAdapter: EpoxyControllerAdapter,
    private val isMarginItem: (Int, EpoxyModel<*>) -> Boolean
) : RecyclerView.ItemDecoration() {
  override fun getItemOffsets(
      outRect: Rect?,
      view: View?,
      parent: RecyclerView?,
      state: RecyclerView.State?) {
    parent?.also {
      val position = it.getChildAdapterPosition(view)
      if (position >= 0
          && isMarginItem(position, epoxyControllerAdapter.getModelAtPosition(position))) {
        outRect?.set(left, top, right, bottom)
      }
    }
  }
}