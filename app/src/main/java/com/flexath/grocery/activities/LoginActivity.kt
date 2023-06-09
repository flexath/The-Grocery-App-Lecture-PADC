package com.flexath.grocery.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.flexath.grocery.R
import com.flexath.grocery.databinding.ActivityLoginBinding
import com.flexath.grocery.mvp.presenters.LoginPresenter
import com.flexath.grocery.mvp.presenters.impls.LoginPresenterImpl
import com.flexath.grocery.mvp.views.LoginView
import com.google.firebase.messaging.FirebaseMessaging

class LoginActivity : BaseActivity(), LoginView {

    private lateinit var binding:ActivityLoginBinding

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    private lateinit var mPresenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))

        setUpPresenter()
        setUpActionListeners()

        mPresenter.onUiReady(this,this)

        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            Log.d("fbToken",it.result)
        }
    }

    private fun setUpActionListeners() {
        binding.btnLogin.setOnClickListener {
            mPresenter.onTapLogin(this,binding.etEmail.text.toString(), binding.etPassword.text.toString())
        }

        binding.btnRegister.setOnClickListener {
            mPresenter.onTapRegister()
        }
    }

    private fun setUpPresenter() {
        mPresenter = getPresenter<LoginPresenterImpl, LoginView>()
    }

    override fun navigateToHomeScreen() {
        startActivity(MainActivity.newIntent(this))
    }

    override fun navigateToRegisterScreen() {
        startActivity(RegisterActivity.newIntent(this))
    }

    override fun showError(error: String) {
        Toast.makeText(this,error,Toast.LENGTH_SHORT).show()
    }
}