package session.hyderabad.live.sessionliveqa.activites

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import session.hyderabad.live.sessionliveqa.AppConstants
import session.hyderabad.live.sessionliveqa.R
import session.hyderabad.live.sessionliveqa.utils.getSavedBoolean
import session.hyderabad.live.sessionliveqa.utils.saveBoolean
import session.hyderabad.live.sessionliveqa.utils.saveString
import session.hyderabad.live.sessionliveqa.utils.showToast


class LoginScreenActivity : AppCompatActivity() {

    companion object {
        const val RC_SIGN_IN = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (getSavedBoolean(AppConstants.LoginUser.USER_LOGGED_IN)) {
            startActivity(Intent(this@LoginScreenActivity, MainActivity::class.java))
            finish()
        } else {
            val providers = listOf(
                    AuthUI.IdpConfig.GoogleBuilder().build(),
                    AuthUI.IdpConfig.EmailBuilder().build(),
                    AuthUI.IdpConfig.PhoneBuilder().build()
            )

            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                    .setTheme(R.style.GreenTheme)
                    .setAvailableProviders(providers).build(), RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser

                when {
                    response?.email.isNullOrEmpty().not() -> {
                        saveString(AppConstants.LoginUser.EMAIL, response?.email.toString())
                        saveString(AppConstants.LoginUser.NAME, user?.displayName.toString())
                    }

                    response?.phoneNumber.isNullOrEmpty().not() -> {
                        saveString(AppConstants.LoginUser.PHONE, response?.phoneNumber.toString())
                    }
                }
                saveBoolean(AppConstants.LoginUser.USER_LOGGED_IN, true)
                startActivity(Intent(this@LoginScreenActivity, MainActivity::class.java))
                finish()
            } else {
                showToast(getString(R.string.error_toast_unable_to_login))
            }
        }
    }
}