package com.udacity.project4.authentication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.udacity.project4.R
import com.udacity.project4.locationreminders.RemindersActivity

/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
class AuthenticationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        val firebaseAuth = FirebaseAuth.getInstance()

        var googleSignInClient: GoogleSignInClient

        findViewById<View>(R.id.logIn_email_button).setOnClickListener {
            LogIn();
        }
        findViewById<View>(R.id.logIn_google_button).setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
            googleSignInClient = GoogleSignIn.getClient(this, gso)
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)

        }
    }


    private fun LogIn() {
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(
                    listOf(
                        AuthUI.IdpConfig.EmailBuilder().build(),
                        AuthUI.IdpConfig.GoogleBuilder().build()
                    )
                )
                .setAuthMethodPickerLayout(
                    AuthMethodPickerLayout
                        .Builder(R.layout.activity_authentication)
                        .setGoogleButtonId(R.id.logIn_google_button)
                        .setEmailButtonId(R.id.logIn_email_button)
                        .build()
                )
                .setTheme(R.style.AppTheme)
                .build(),
            SIGN_IN_RESULT_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != SIGN_IN_RESULT_CODE) {
            Toast.makeText(this, getString(R.string.Login_failed), Toast.LENGTH_LONG).show()
            return
        }
        if (resultCode == RESULT_OK) {
            Toast.makeText(this, getString(R.string.Successful_login), Toast.LENGTH_LONG).show()
            goToReminderActivity()
        }
    }

    private fun goToReminderActivity() {
        val intent = Intent(this, RemindersActivity::class.java)
        startActivity(intent)
        finish()
        return
    }

    companion object {
        const val SIGN_IN_RESULT_CODE = 1001
        const val RC_SIGN_IN = 100
    }
}
