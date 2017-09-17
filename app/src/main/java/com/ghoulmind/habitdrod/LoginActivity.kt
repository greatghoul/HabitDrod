package com.ghoulmind.habitdrod

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ghoulmind.habitica.HabiticaClient
import com.ghoulmind.habitica.HabiticaException

import kotlinx.android.synthetic.main.activity_login.*

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

        try {
            val data = apiClient.login(username, password)
            // TODO: Store api key and user id
        } catch (e: HabiticaException) {
            // TODO: Show user the errors
        }

    }
}
