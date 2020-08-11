package com.malfliet.hangaround.login

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.auth0.android.Auth0Exception
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.authentication.storage.CredentialsManagerException
import com.auth0.android.callback.BaseCallback
import com.auth0.android.provider.AuthCallback
import com.auth0.android.provider.VoidCallback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.malfliet.hangaround.MainActivity
import com.malfliet.hangaround.R
import com.malfliet.hangaround.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        viewModel = ViewModelProviders.of(this, LoginViewModel.LoginViewModelFactory(application))
            .get(LoginViewModel::class.java)

        if (intent.getBooleanExtra("CLEAR_CREDENTIALS", false)) {
            logout()
            return
        }

        if (viewModel.hasValidCredentials()) {
            goToMainActivity()
        }
    }

    private fun logout() {
        WebAuthProvider.logout(viewModel.auth0)
            .withScheme("demo")
            .start(this, object : VoidCallback {
                override fun onSuccess(payload: Void?) {
                    viewModel.credentialsManager.clearCredentials()
                }

                override fun onFailure(error: Auth0Exception) {
                    Toast.makeText(applicationContext, "failed to log user out", Toast.LENGTH_LONG)
                        .show()
                    goToMainActivity()
                }
            })
    }

    private fun goToMainActivity() {
        viewModel.credentialsManager.getCredentials(object :
            BaseCallback<Credentials, CredentialsManagerException> {

            override fun onSuccess(credentials: Credentials?) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.putExtra("ACCESS_TOKEN", credentials!!.accessToken)
                startActivity(intent)
                finish()
            }

            override fun onFailure(error: CredentialsManagerException?) {
                finish()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        binding.buttonLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
        binding.buttonLogin.isEnabled = false
        binding.buttonLogin.isClickable = false
        WebAuthProvider.login(viewModel.auth0)
            .withScheme("demo")
            .withAudience(
                String.format(
                    "https://%s/userinfo",
                    getString(R.string.com_auth0_domain)
                )
            )
            .start(this@LoginActivity, object : AuthCallback {
                override fun onFailure(dialog: Dialog) {
                    // Show error Dialog to user
                    binding.buttonLogin.isEnabled = true
                    binding.buttonLogin.isClickable = true
                    runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            "Error Logging In.",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }

                override fun onFailure(exception: AuthenticationException) {
                    binding.buttonLogin.isEnabled = true
                    binding.buttonLogin.isClickable = true
                    runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            "Something Went Wrong!",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }

                override fun onSuccess(credentials: Credentials) {
                    try {
                        viewModel.login(credentials.accessToken!!)
                        viewModel.credentialsManager.saveCredentials(credentials)
                        goToMainActivity()
                    } catch (e: Exception) {
                        runOnUiThread {
                            binding.buttonLogin.isEnabled = true
                            binding.buttonLogin.isClickable = true
                        }
                    }
                }
            })
    }
}