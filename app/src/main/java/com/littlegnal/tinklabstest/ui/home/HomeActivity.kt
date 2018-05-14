package com.littlegnal.tinklabstest.ui.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.util.SparseArray
import com.littlegnal.tinklabstest.R.layout
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_home.tl_home
import kotlinx.android.synthetic.main.activity_home.vp_home
import javax.inject.Inject

class HomeActivity : AppCompatActivity(), HasSupportFragmentInjector {

  class HomeFragmentPagerAdapter(
    fragmentManager: FragmentManager,
    private val titles: Array<Pair<String, String>>
  ) : FragmentPagerAdapter(fragmentManager) {

    private val fragments: SparseArray<Fragment> by lazy {
      SparseArray<Fragment>()
    }

    override fun getItem(position: Int): Fragment {
      val fragment = fragments.get(position)
      if (fragment != null) {
        return fragment
      }

      val category = titles[position].second
      val homeFragment = HomeFragment.newInstance(category)
      fragments.put(position, homeFragment)
      return homeFragment
    }

    override fun getCount(): Int = titles.size

    override fun getPageTitle(position: Int): CharSequence {
      return titles[position].first
    }
  }

  @Inject lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

  override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_home)

    vp_home.adapter = HomeFragmentPagerAdapter(
        supportFragmentManager,
        arrayOf(
            Pair("CITY GUIDE", "city"),
            Pair("SHOP", "shop"),
            Pair("EAT", "eat")))
    vp_home.offscreenPageLimit = 3
    tl_home.setupWithViewPager(vp_home)
  }
}
