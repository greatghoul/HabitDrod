package com.ghoulmind.habitdrod

import android.content.Context
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ghoulmind.habitica.HabiticaClient
import com.ghoulmind.habitica.HabiticaException
import kotlinx.android.synthetic.main.activity_tasks.*
import org.json.JSONArray
import org.json.JSONObject

class TasksActivity : AppCompatActivity() {
    val apiClient = HabiticaClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)
        fetchTasks()
    }

    private fun fetchTasks() {
        val sharedPre =  getSharedPreferences(getString(R.string.app_prefs_file), Context.MODE_PRIVATE)
        var apiKey = sharedPre.getString("apiToken", "no-token")
        var apiUser = sharedPre.getString("id", "no-id")

        GetTasksTask(apiKey, apiUser).execute()
    }

    inner class GetTasksTask internal constructor(private val apiKey: String, private val apiUser: String) : AsyncTask<Void, Void, JSONArray?>() {
        override fun doInBackground(vararg params: Void): JSONArray? {
            try {
                return apiClient.getTasks(apiKey, apiUser)
            } catch (e: HabiticaException) {
                return null
            }
        }

        override fun onPostExecute(data: JSONArray?) {
            if (data == null) {
                Toast.makeText(applicationContext, R.string.error_get_tasks, Toast.LENGTH_LONG).show()
            } else {
                tasksCount.text = data.length().toString()
            }
        }
    }
}
