package com.flexath.grocery.mvp.presenters.impls

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.flexath.grocery.analytics.PARAMETER_EMAIL
import com.flexath.grocery.analytics.SCREEN_REGISTER
import com.flexath.grocery.analytics.TAP_REGISTER
import com.flexath.grocery.data.models.AuthenticationModel
import com.flexath.grocery.data.models.AuthenticationModelImpl
import com.flexath.grocery.mvp.presenters.AbstractBasePresenter
import com.flexath.grocery.mvp.presenters.RegisterPresenter
import com.flexath.grocery.mvp.views.RegisterView

class RegisterPresenterImpl : RegisterPresenter, AbstractBasePresenter<RegisterView>() {

    private val mAuthenticationModel: AuthenticationModel = AuthenticationModelImpl

    override fun onTapRegister(context: Context,email: String, password: String, userName: String) {
        sendEventsToFirebaseAnalytics(context, TAP_REGISTER, PARAMETER_EMAIL,email)
        mAuthenticationModel.register(email, password, userName, onSuccess = {
            mView.navigateToToLoginScreen()
        }, onFailure = {
            mView.showError(it)
        })
    }

    override fun onUiReady(context: Context, owner: LifecycleOwner) {
        sendEventsToFirebaseAnalytics(context, SCREEN_REGISTER)
    }
}