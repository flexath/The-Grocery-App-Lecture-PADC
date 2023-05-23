package com.flexath.grocery.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.flexath.grocery.R
import com.flexath.grocery.databinding.ActivityLoginBinding
import com.flexath.grocery.mvp.presenters.LoginPresenter
import com.flexath.grocery.mvp.presenters.impls.LoginPresenterImpl
import com.flexath.grocery.mvp.views.LoginView

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
    }

    private fun setUpActionListeners() {
        binding.btnLogin.setOnClickListener {
            mPresenter.onTapLogin(binding.etEmail.text.toString(), binding.etPassword.text.toString())
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

    }
}