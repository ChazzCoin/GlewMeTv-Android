package io.aokihome.glewmetv

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.aokihome.glewmetv.ui.navbar.NavBar


class MainActivity : AppCompatActivity() {

    var navBar: NavBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        // -> Init Navigation Bar Setup
        this.navBar = NavBar(this)

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
//            riptDispatcher.launch {
//                mainDispatcher.launch {
//                    hookupAdapter?.listOfHookups = null
//                    hookupAdapter?.notifyDataSetChanged()
//                }
//                initReload()
//            }
        }

    }

    fun getFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.hookupFragment, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}