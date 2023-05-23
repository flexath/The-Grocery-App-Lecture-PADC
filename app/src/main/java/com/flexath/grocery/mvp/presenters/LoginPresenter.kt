package com.flexath.grocery.mvp.presenters

import com.flexath.grocery.mvp.views.LoginView

interface LoginPresenter : BasePresenter<LoginView>{
    fun onTapLogin(email: String, password: String)
    fun onTapRegister()
}