package io.aokihome.glewmetv.ui.navbar

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import devlight.io.library.ntb.NavigationTabBar
import io.aokihome.glewmetv.MainActivity
import io.aokihome.glewmetv.R
import io.aokihome.glewmetv.ui.DashboardFragment
import io.aokihome.glewmetv.ui.MetaReportFragment

class NavBar(val mainActivity:MainActivity) {

    val navigationTabBar = mainActivity.findViewById<View>(R.id.ntb) as NavigationTabBar
    private val viewPager = mainActivity.findViewById<ViewPager>(R.id.viewPager)
    private val models: ArrayList<NavigationTabBar.Model> = ArrayList()

    init {
        // -> Create button for each fragment
        val dashboardButton = buildTabButton("Dashboard", mainActivity.resources.getDrawable(R.drawable.ic_launcher_background))
        val metaReportButton = buildTabButton("MetaReport", mainActivity.resources.getDrawable(R.drawable.ic_launcher_background))
        models.add(dashboardButton)
        models.add(metaReportButton)
        viewPager.adapter = PageAdapter(mainActivity.supportFragmentManager, models.size)
        navigationTabBar.models = models
        navigationTabBar.setViewPager(viewPager, 0)
    }

    private fun buildTabButton(title:String, image:Drawable) : NavigationTabBar.Model {
        return NavigationTabBar.Model
            .Builder(image, Color.RED)
            .title(title)
            .badgeTitle("NTB")
            .build()
    }

    inner class PageAdapter(fm: FragmentManager, private val modelCount:Int) : FragmentPagerAdapter(fm) {
        override fun getCount(): Int {
            return modelCount
        }

        override fun getItem(position: Int): Fragment {
            return when(position) {
                0 -> DashboardFragment()
                1 -> MetaReportFragment(mainActivity)
                else -> DashboardFragment()
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when(position) {
                0 -> return "Dashboard"
                1 -> return "Meta Report"
                2 -> return "Tab 3"
            }
            return super.getPageTitle(position)
        }

    }


}

