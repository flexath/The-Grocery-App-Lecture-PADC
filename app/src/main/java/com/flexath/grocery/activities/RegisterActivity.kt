package com.flexath.grocery.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.flexath.grocery.databinding.ActivityRegisterBinding
import com.flexath.grocery.mvp.presenters.RegisterPresenter
import com.flexath.grocery.mvp.presenters.impls.RegisterPresenterImpl
import com.flexath.grocery.mvp.views.RegisterView

class RegisterActivity : BaseActivity(), RegisterView {

    private lateinit var binding:ActivityRegisterBinding

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, RegisterActivity::class.java)
        }
    }

    private lateinit var mPresenter: RegisterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpPresenter()
        setUpActionListeners()
    }

    private fun setUpActionListeners() {
        binding.btnRegister.setOnClickListener {
            mPresenter.onTapRegister(
                this,
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString(),
                binding.etUserName.text.toString()
            )
        }
    }

    private fun setUpPresenter() {
        mPresenter = getPresenter<RegisterPresenterImpl, RegisterView>()
    }

    override fun navigateToToLoginScreen() {
        startActivity(LoginActivity.newIntent(this))
    }

    override fun showError(error: String) {

    }
}