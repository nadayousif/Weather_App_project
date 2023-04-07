package com.example.weatherappproject

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherappproject.databinding.ActivityMainBinding
import com.example.weatherappproject.remoteData.RemoteDataSource
import com.example.weatherappproject.util.ConnectionUtils
import com.example.weatherappproject.util.MySharedPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*
import android.content.res.Configuration
import android.os.Build
import android.text.TextUtils
import android.view.View

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    var layoutDirection:Int?= null
    companion object{
        var isNotOpen=true;
    }
    override fun attachBaseContext(newBase: Context?) {
        if (newBase != null && isNotOpen) {
            MySharedPreference.getInstance(newBase)
            setLocal(MySharedPreference.getLanguage(), newBase)
            isNotOpen=false
        }
        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (layoutDirection!=null)
            window.decorView.layoutDirection = layoutDirection!!

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        /*binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }*/
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_favorite, R.id.nav_settings,R.id.alertFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        ConnectionUtils.initialize(this.applicationContext.getSystemService(ConnectivityManager::class.java))
       var remoteSource: RemoteDataSource = RemoteDataSource()
        MainScope().launch(Dispatchers.IO){
            var data =remoteSource.getWeatherDataOnline(31.2001,29.9187,MySharedPreference.getLanguage(),MySharedPreference.getUnits())
            Log.i("nada", "onCreate: ${data.body().toString()}")
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    private fun setLocal(lang: String, newBase: Context) {
        val local = Locale(lang)
        Locale.setDefault(local)
        val config = Configuration()
        config.setLocale(local)
        newBase.resources.updateConfiguration(
            config,
            newBase.resources.displayMetrics
        )

        // Determine layout direction based on selected language
        layoutDirection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (TextUtils.getLayoutDirectionFromLocale(local) == View.LAYOUT_DIRECTION_RTL) {
                View.LAYOUT_DIRECTION_RTL
            } else {
                View.LAYOUT_DIRECTION_LTR
            }
        } else {
            View.LAYOUT_DIRECTION_LTR
        }

    }
}