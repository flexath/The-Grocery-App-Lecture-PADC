package com.flexath.grocery.mvp.presenters.impls

import androidx.lifecycle.LifecycleOwner
import com.flexath.grocery.data.models.AuthenticationModel
import com.flexath.grocery.data.models.AuthenticationModelImpl
import com.flexath.grocery.mvp.presenters.AbstractBasePresenter
import com.flexath.grocery.mvp.presenters.LoginPresenter
import com.flexath.grocery.mvp.views.LoginView

class LoginPresenterImpl : LoginPresenter, AbstractBasePresenter<LoginView>() {

    private val mAuthenticationModel: AuthenticationModel = AuthenticationModelImpl

    override fun onUiReady(owner: LifecycleOwner) {}

    override fun onTapLogin(email: String, password: String) {
        mAuthenticationModel.login(email, password, onSuccess = {
            mView.navigateToHomeScreen()
        }, onFailure = {
            mView.showError(it)
        })
    }

    override fun onTapRegister() {
        mView.navigateToRegisterScreen()
    }
}