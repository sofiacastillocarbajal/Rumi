package pe.edu.upc.rumi.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import pe.edu.upc.rumi.R
import pe.edu.upc.rumi.fragments.MyGroupFragment
import pe.edu.upc.rumi.fragments.MyIncidentsFragment
import pe.edu.upc.rumi.fragments.MyTasksFragment

class MainRoomerActivity : AppCompatActivity() {
    companion object {
        const val FRAGMENT_GROUPS = 1
        const val FRAGMENT_INCIDENCIAS = 2
        const val FRAGMENT_TAREAS = 3
        var CURRENT_FRAGMENT_SELECTED = 1
    }

    private lateinit var navView: BottomNavigationView
    private lateinit var addFloatingActionButton: FloatingActionButton

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        navigateTo(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_roomer)
        supportActionBar?.hide()

        addFloatingActionButton = findViewById(R.id.addFloatingActionButton)
        addFloatingActionButton.setOnClickListener {
            addAction()
        }

        navView = findViewById(R.id.navigation)
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigateTo(navView.menu.findItem(R.id.navigation_group_roomer))
    }

    private fun getFragmentFor(item: MenuItem): Fragment {
        return when(item.itemId) {
            R.id.navigation_group_roomer -> {
                CURRENT_FRAGMENT_SELECTED = FRAGMENT_GROUPS
                addFloatingActionButton.hide()
                MyGroupFragment()
            }
            R.id.navigation_incidencia_roomer -> {
                CURRENT_FRAGMENT_SELECTED = FRAGMENT_INCIDENCIAS
                addFloatingActionButton.show()
                MyIncidentsFragment()
            }
            R.id.navigation_tarea_roomer -> {
                CURRENT_FRAGMENT_SELECTED = FRAGMENT_TAREAS
                addFloatingActionButton.hide()
                MyTasksFragment()
            }
            else -> {
                CURRENT_FRAGMENT_SELECTED = FRAGMENT_GROUPS
                addFloatingActionButton.hide()
                MyGroupFragment()
            }
        }
    }

    private fun navigateTo(item: MenuItem): Boolean {
        item.isChecked = true

        return supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_container, getFragmentFor(item))
            .commit() > 0
    }

    fun addAction() {
        if (CURRENT_FRAGMENT_SELECTED == FRAGMENT_INCIDENCIAS){
            val intent = Intent(this, CreateIncidenceActivity::class.java)
            startActivity(intent)
        }
    }
}
