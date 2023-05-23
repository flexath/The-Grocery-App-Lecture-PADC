package com.flexath.grocery.mvp.presenters

import com.flexath.grocery.mvp.views.RegisterView

interface RegisterPresenter : BasePresenter<RegisterView> {
    fun onTapRegister(email: String, password: String, userName: String)
}