package com.flexath.grocery.mvp.presenters.impls

import androidx.lifecycle.LifecycleOwner
import com.flexath.grocery.data.models.AuthenticationModel
import com.flexath.grocery.data.models.AuthenticationModelImpl
import com.flexath.grocery.mvp.presenters.AbstractBasePresenter
import com.flexath.grocery.mvp.presenters.RegisterPresenter
import com.flexath.grocery.mvp.views.RegisterView

class RegisterPresenterImpl : RegisterPresenter, AbstractBasePresenter<RegisterView>() {

    private val mAuthenticationModel: AuthenticationModel = AuthenticationModelImpl

    override fun onTapRegister(email: String, password: String, userName: String) {
        mAuthenticationModel.register(email, password, userName, onSuccess = {
            mView.navigateToToLoginScreen()
        }, onFailure = {
            mView.showError(it)
        })
    }

    override fun onUiReady(owner: LifecycleOwner) {}
}