package com.ghoulmind.habitdrod

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ghoulmind.habitica.HabiticaClient
import com.ghoulmind.habitica.HabiticaException

import kotlinx.android.synthetic.main.activity_login.*
import android.os.AsyncTask
import org.json.JSONObject


/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {
    val apiClient = HabiticaClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initLoginForm()
    }

    private fun initLoginForm() {
        btnSignin.setOnClickListener { attemptLogin() }
    }

    private fun attemptLogin() {
        var username = inputUsername.text.toString()
        var password = inputPassword.text.toString()

        UserLoginTask(username, password).execute()
    }

    private fun getPrefsEditor(): SharedPreferences.Editor {
        val prefs = getSharedPreferences(getString(R.string.app_prefs_file), Context.MODE_PRIVATE)
        return prefs.edit()
    }

    private fun saveUserCredentials(id: String, apiToken: String) {
        val editor = getPrefsEditor()
        editor.putString("id", id)
        editor.putString("apiToken", apiToken)
        editor.commit()
    }

    inner class UserLoginTask internal constructor(private val username: String, private val password: String) : AsyncTask<Void, Void, JSONObject?>() {
        override fun doInBackground(vararg params: Void): JSONObject? {
            try {
                return apiClient.login(username, password)
            } catch (e: HabiticaException) {
                return null
            }
        }

        override fun onPostExecute(data: JSONObject?) {
            if (data == null) {
                inputPassword.requestFocus()
                Toast.makeText(applicationContext, R.string.error_signin_invalid, Toast.LENGTH_LONG).show()
            } else {
                saveUserCredentials(data.getString("id"), data.getString("apiToken"))
                Toast.makeText(applicationContext, "Welcome", Toast.LENGTH_LONG).show()
            }
        }
    }
}
