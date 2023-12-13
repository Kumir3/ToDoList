package com.hfad.todolist

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.hfad.todolist.databinding.ActivityMainBinding
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.room.Room
import com.hfad.todolist.database.Database
import com.hfad.todolist.database.Entity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var taskDatabase: Database
    private lateinit var editTextTask: EditText
    private lateinit var buttonAdd: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editTextTask = findViewById(R.id.editTextTask)
        buttonAdd = findViewById(R.id.buttonAdd)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        taskDatabase = Room.databaseBuilder(
            applicationContext,
            Database::class.java, "task-database"
        ).build()

        buttonAdd.setOnClickListener { addTask() }

        loadTasks()
    }

    private fun addTask() {
        val task = editTextTask.text.toString()
        if (task.isNotEmpty()) {
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    taskDatabase.taskDao().insertTask(Entity(task = task))
                }
                withContext(Dispatchers.Main) {
                    tasks.add(task)
                    arrayAdapter.notifyDataSetChanged()
                    editTextTask.text.clear()
                }
            }
        }
    }

    private fun loadTasks() {
        GlobalScope.launch {
            val loadedTasks = withContext(Dispatchers.IO) {
                taskDatabase.taskDao().getAllTasks().map { it.task }
            }
            withContext(Dispatchers.Main) {
                tasks.addAll(loadedTasks)
                arrayAdapter.notifyDataSetChanged()
            }
        }
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

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }



}