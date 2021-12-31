package io.aokihome.glewmetv

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import io.aokihome.glewmetv.db.*
import io.aokihome.glewmetv.http.GmtHttpRequest
import io.aokihome.glewmetv.ui.MainGlewMeTvActivity
import io.aokihome.glewmetv.utils.*
import io.realm.Realm
import io.realm.RealmConfiguration


class MainActivity : AppCompatActivity() {

    companion object {
        var session: Session? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Init Realm DB
        Realm.init(this)
        val realmConfiguration = RealmConfiguration.Builder()
            .name(BuildConfig.APPLICATION_ID + ".realm")
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(realmConfiguration)
//        io {
//            println("starting1")
//            await {
//                loadHookups()
//            }
//            println("finished!")
//            main {
//                startActivity(Intent(this@MainActivity, MainGlewMeTvActivity::class.java))
//            }
//        }
//        io {
//            loadGlewMeTvData()
//        }
        startActivity(Intent(this@MainActivity, MainGlewMeTvActivity::class.java))
    }


    private suspend fun loadGlewMeTvData() {
        val response = GmtHttpRequest().getAsync(GmtHttpRequest.URL_GLEWMETV_DATA).await()
        val temp = Parser.AllDataPackages(response)
        println(temp)
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