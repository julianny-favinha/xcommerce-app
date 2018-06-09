package com.xcommerce.mc920.xcommerce

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.xcommerce.mc920.xcommerce.cart.CartActivity
import com.xcommerce.mc920.xcommerce.home.TabPagerAdapter
import com.xcommerce.mc920.xcommerce.model.Address
import com.xcommerce.mc920.xcommerce.model.AddressFull
import com.xcommerce.mc920.xcommerce.model.User
import com.xcommerce.mc920.xcommerce.myorders.MyOrdersActivity
import com.xcommerce.mc920.xcommerce.user.UserHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import android.content.SharedPreferences
import android.os.AsyncTask
import com.xcommerce.mc920.xcommerce.model.UserAPI
import com.xcommerce.mc920.xcommerce.utilities.ClientHttpUtil


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        configureTabLayout()

        val preferences = getSharedPreferences("com.xcommerce.mc920.xcommerce", Context.MODE_PRIVATE)

        val token = preferences.getString("tokenUser", "")

        if (token != "") {
            UserHelper.token = token
            GetUserTask().execute()
        }

        main_search_button.setOnClickListener{
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        invalidateOptionsMenu()
    }

    private fun configureTabLayout() {
        // add tabs
        tab_layout.addTab(tab_layout.newTab().setText("DESTAQUES"));
        tab_layout.addTab(tab_layout.newTab().setText("CATEGORIAS"));

        val adapter = TabPagerAdapter(supportFragmentManager, tab_layout.tabCount)
        pager.adapter = adapter

        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_layout))

        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        if (UserHelper.isLoggedIn()){
            val usr = UserHelper.retrieveUser()
            findViewById<TextView>(R.id.nav_header_name).apply { text = usr.name }
            findViewById<TextView>(R.id.nav_header_email).apply { text = usr.email }
        } else {
            findViewById<TextView>(R.id.nav_header_name).apply { text = "" }
            findViewById<TextView>(R.id.nav_header_email).apply { text = "" }
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation item item clicks here.
        when (item.itemId) {
            R.id.nav_meuspedidos -> {
                val intent = Intent(this, MyOrdersActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_sac -> {

            }
            R.id.nav_login -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

            R.id.nav_carrinho -> {
                val intent = Intent(this, CartActivity::class.java)
                startActivity(intent)
            }

            R.id.nav_profile -> {
                val intent = Intent(this, UserProfileActivity::class.java)
                startActivity(intent)
            }

            R.id.nav_logout -> {
                UserHelper.logOut()
                invalidateOptionsMenu()
            }
            //TODO: Remove when backend integration is done
            R.id.debug_login -> {
                val address = AddressFull(Address("17020510","Alameda das hortensias", "PVA", "Bauru", "SP"), 851, "1")
                UserHelper.updateUser(User("Ronaldo Prata Amorim", "ronaldo@g.com", "123mudar","13/06/1994","35028504812", address, "M", "99999999"))
                invalidateOptionsMenu()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    inner class GetUserTask internal constructor(): AsyncTask<Void, Void, User>() {

        override fun doInBackground(vararg params: Void): User? {
            return ClientHttpUtil.getRequest(UserAPI.User.PATH, true)
        }

        override fun onPostExecute(user: User?) {
            UserHelper.updateUser(user)
            invalidateOptionsMenu()
        }

        override fun onCancelled() {

        }
    }
}
