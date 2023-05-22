package com.flexath.grocery.mvp.presenters

import androidx.lifecycle.LifecycleOwner
import com.flexath.grocery.mvp.views.BaseView

interface BasePresenter<V: BaseView> {
    fun onUiReady(owner: LifecycleOwner)
    fun initPresenter(view: V)
}