package com.flexath.grocery.mvp.presenters.impls

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.flexath.grocery.analytics.PARAMETER_EMAIL
import com.flexath.grocery.analytics.SCREEN_LOGIN
import com.flexath.grocery.analytics.TAP_LOGIN
import com.flexath.grocery.data.models.AuthenticationModel
import com.flexath.grocery.data.models.AuthenticationModelImpl
import com.flexath.grocery.data.models.GroceryModel
import com.flexath.grocery.data.models.GroceryModelImpl
import com.flexath.grocery.mvp.presenters.AbstractBasePresenter
import com.flexath.grocery.mvp.presenters.LoginPresenter
import com.flexath.grocery.mvp.views.LoginView

class LoginPresenterImpl : LoginPresenter, AbstractBasePresenter<LoginView>() {

    private val mAuthenticationModel: AuthenticationModel = AuthenticationModelImpl
    private val mGroceryModel:GroceryModel = GroceryModelImpl

    override fun onUiReady(context: Context, owner: LifecycleOwner) {
        sendEventsToFirebaseAnalytics(context,SCREEN_LOGIN)
        mGroceryModel.setUpRemoteConfigWithDefaultValues()
        mGroceryModel.fetchRemoteConfigs()
    }

    override fun onTapLogin(context: Context,email: String, password: String) {
        sendEventsToFirebaseAnalytics(context, TAP_LOGIN, PARAMETER_EMAIL,email)
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