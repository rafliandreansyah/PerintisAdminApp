package com.azhara.perintisadminapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.azhara.perintisadminapp.R
import com.azhara.perintisadminapp.databinding.ActivityHomeBinding
import com.azhara.perintisadminapp.ui.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarHome.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_dashboard, R.id.nav_car, R.id.nav_tour, R.id.nav_admin, R.id.nav_user, R.id.nav_mitra,
                    R.id.nav_booking_car, R.id.nav_booking_tour, R.id.nav_change_password, R.id.nav_mitra_register,
                    R.id.nav_register_car
            ), drawerLayout
        )

        navView.menu.findItem(R.id.nav_logout).setOnMenuItemClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            this.finish()
            false
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun isLoading(state: Boolean){
        if(state){
            binding.appBarHome.contentHome.loadingHome.visibility = View.VISIBLE
        }else{
            binding.appBarHome.contentHome.loadingHome.visibility = View.INVISIBLE
        }
    }
}