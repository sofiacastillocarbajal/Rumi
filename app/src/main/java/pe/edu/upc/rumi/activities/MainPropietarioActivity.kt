package pe.edu.upc.rumi.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import pe.edu.upc.rumi.R
import pe.edu.upc.rumi.fragments.GroupsFragment
import pe.edu.upc.rumi.fragments.IncidentsFragment
import pe.edu.upc.rumi.fragments.TasksFragment

class MainPropietarioActivity : AppCompatActivity() {
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
        setContentView(R.layout.activity_main_propietario)
        supportActionBar?.hide()

        addFloatingActionButton = findViewById(R.id.addFloatingActionButton)
        addFloatingActionButton.setOnClickListener {
            addAction()
        }

        navView = findViewById(R.id.navigation)
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigateTo(navView.menu.findItem(R.id.navigation_group))
    }

    private fun getFragmentFor(item: MenuItem): Fragment {
        return when(item.itemId) {
            R.id.navigation_group -> {
                CURRENT_FRAGMENT_SELECTED = FRAGMENT_GROUPS
                addFloatingActionButton.show()
                GroupsFragment()
            }
            R.id.navigation_incidencia -> {
                CURRENT_FRAGMENT_SELECTED = FRAGMENT_INCIDENCIAS
                addFloatingActionButton.hide()
                IncidentsFragment()
            }
            R.id.navigation_tarea -> {
                CURRENT_FRAGMENT_SELECTED = FRAGMENT_TAREAS
                addFloatingActionButton.show()
                TasksFragment()
            }
            else -> {
                CURRENT_FRAGMENT_SELECTED = FRAGMENT_GROUPS
                addFloatingActionButton.show()
                GroupsFragment()
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
        if (CURRENT_FRAGMENT_SELECTED == FRAGMENT_GROUPS){
            val intent = Intent(this, AddGroupActivity::class.java)
            startActivity(intent)
        }
        else if (CURRENT_FRAGMENT_SELECTED == FRAGMENT_TAREAS){
            navigateTo(navView.menu.findItem(R.id.navigation_group))
            Toast.makeText(this, "Escoja un grupo para crear una tarea", Toast.LENGTH_LONG).show()
        }
    }
}
